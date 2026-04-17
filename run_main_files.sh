java -cp data-integration-project.jar main.SetupTablesMain -d company -l /home/logs
java -cp data-integration-project.jar main.SampleDataMain -t employee -l /home/logs -o /home/sample_data/employee -n 1000
java -cp data-integration-project.jar main.SampleDataMain -t customer_event -l /home/logs -o /home/sample_data/customer_event -n 10000

sleep 10 && echo "Waiting for 10 seconds before proceeding with next java programs"
java -cp data-integration-project.jar main.SampleDataMain -t customer_event -l /home/logs -o /home/sample_data/customer_event -n 10000
java -cp data-integration-project.jar main.SampleDataMain -t employee -l /home/logs -o /home/sample_data/employee -n 1000

sleep 10 && echo "Waiting for 10 seconds before proceeding with next java programs"
java -cp data-integration-project.jar main.InsertFromDirectoryMain -d company -l /home/logs -t employee -i /home/sample_data/employee
java -cp data-integration-project.jar main.InsertFromDirectoryMain -d company -l /home/logs -t customer_event -i /home/sample_data/customer_event
