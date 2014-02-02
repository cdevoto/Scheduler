/usr/local/mysql/bin/mysql -u root -pclaudia2 apmng < create_tables.sql
echo "Tables created successfully!"
/usr/local/mysql/bin/mysql -u root -pclaudia2 apmng < load_data.sql
echo "Data loaded successfully!"