java -cp database-project.jar main.SetupTablesMain -d company -c 1 -e 1
java -cp database-project.jar main.SampleDataMain -i employee -o /home/sample_data/employee -s 1000 -id 0
java -cp database-project.jar main.SampleDataMain -i employee -o /home/sample_data/employee -s 1000 -id 1000
java -cp database-project.jar main.SampleDataMain -i customer_event -o /home/sample_data/customer_event -s 100000 -id 0
java -cp database-project.jar main.SampleDataMain -i customer_event -o /home/sample_data/customer_event -s 100000 -id 100000

sleep 10 && echo "Waiting for 10 seconds before proceeding with next java programs"
java -cp database-project.jar main.InsertFromDirectoryMain -d company -t employee -i /home/sample_data/employee
java -cp database-project.jar main.InsertFromDirectoryMain -d company -t customer_event -i /home/sample_data/customer_event
