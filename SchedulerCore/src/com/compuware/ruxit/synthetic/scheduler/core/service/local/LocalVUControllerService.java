package com.compuware.ruxit.synthetic.scheduler.core.service.local;

import static com.compuware.ruxit.synthetic.scheduler.core.service.util.AbilityFlagUtil.decodeAbilityFlags;
import static com.compuware.ruxit.synthetic.scheduler.core.util.ExceptionUtil.launderThrowable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compuware.ruxit.synthetic.scheduler.core.dao.AbilityFlagDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.LcpDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.VUControllerDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.LcpProxy;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.VUController;
import com.compuware.ruxit.synthetic.scheduler.core.service.VUControllerService;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIAbilityFlag;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UILcpProxy;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIVUController;
import com.compuware.ruxit.synthetic.scheduler.core.util.ExpirableEntry;

@Component
public class LocalVUControllerService implements VUControllerService {

    private final ConcurrentMap<Long, Future<ExpirableEntry<UIVUController>>> vucs = new ConcurrentHashMap<>();
	private long cacheExpiration = 60 * 60 * 1000;
	private long sweeperInterval = 5 * 60 * 1000;
	
	@Autowired
	private VUControllerDao vucDao;
	@Autowired
	private AbilityFlagDao abilityFlagDao;
	@Autowired
	private LcpDao lcpDao;

	@Override
	public UIVUController getVUController(long vucId) {
    	while (true) {
	        Future<ExpirableEntry<UIVUController>> vuc = vucs.get(vucId);
	        if (vuc == null) {
	        	GetVUController eval = new GetVUController(vucId);
	        	FutureTask<ExpirableEntry<UIVUController>> newVuc = new FutureTask<ExpirableEntry<UIVUController>>(eval);
	            vuc = vucs.putIfAbsent(vucId, newVuc);
	            // vuc has a value, then the put was rejected and the method returned the already existing value associated with the key
	            // if vuc does not have a value, then the put was accepted, so newFlavor is associated with the key and should be returned.   
	            if (vuc == null) {  
	            	vuc = newVuc;
	            	newVuc.run();
	            }
	        }
	        try {
	        	ExpirableEntry<UIVUController> entry = vuc.get();
	        	if (entry != null) {
	        		return entry.value();
	        	}
	            return null;
	        } catch (InterruptedException e) {
	        	Thread.currentThread().interrupt();
	    	} catch (CancellationException e) {
	    		vucs.remove(vucId, vuc);
			} catch (ExecutionException e) {
				throw launderThrowable(e.getCause());
			}
    	}
	}
	
	public LocalVUControllerService () {
		Thread cacheSweeper = new Thread(new Runnable () {

			@Override
			public void run() {
				boolean terminated = false;
				while (!terminated) {
					long now = System.currentTimeMillis();
					for (Map.Entry<Long, Future<ExpirableEntry<UIVUController>>> vuc : vucs.entrySet()) {
						try {
							ExpirableEntry<UIVUController> entry = vuc.getValue().get();
							if (entry.isExpired(now)) {
								vucs.remove(vuc.getKey());
							}
						} catch (InterruptedException e) {
				        	terminated = true;
				        	break;
						} catch (Exception e) {}
					}
					try {
						Thread.sleep(sweeperInterval);
					} catch (InterruptedException e) {
						terminated = true;
					}
			    }
			}
			
		});
		cacheSweeper.setDaemon(true);
		cacheSweeper.start();
	}
	
     private class GetVUController implements Callable<ExpirableEntry<UIVUController>> {
    	private long vucId;
    	
    	private GetVUController (long vucId) {
    		this.vucId = vucId;
    	}

		@Override
		public ExpirableEntry<UIVUController> call() throws Exception {
			VUController daoVuc = vucDao.getById(vucId);
			if (daoVuc == null) {
				return null;
			}
			UIVUController.Builder builder = UIVUController.create()
					.withVuController(daoVuc); 
			
			List<LcpProxy> lcps = lcpDao.getByVucId(vucId);
			for (LcpProxy lcp : lcps) {
				UILcpProxy uiLcp = UILcpProxy.create()
						.withLcpProxy(lcp)
						.build();
				builder.withLcp(uiLcp);
			}

			List<UIAbilityFlag> supportsFlags = decodeAbilityFlags(abilityFlagDao, daoVuc.getSupportsF());
			UIVUController uiVuc = builder
					.withSupportsFlags(supportsFlags)
					.build();
			
			return new ExpirableEntry<>(System.currentTimeMillis() + cacheExpiration, uiVuc);
		}
    	
    }

}
