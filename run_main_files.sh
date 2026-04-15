java -cp data-integration-project.jar main.SetupTablesMain -d company -l /home/logs -c 1 -e 1
java -cp data-integration-project.jar main.SampleDataMain -i employee -l /home/logs -o /home/sample_data/employee -s 1000 -id 0
java -cp data-integration-project.jar main.SampleDataMain -i employee -l /home/logs -o /home/sample_data/employee -s 1000 -id 1000
java -cp data-integration-project.jar main.SampleDataMain -i customer_event -l /home/logs -o /home/sample_data/customer_event -s 100000 -id 0
java -cp data-integration-project.jar main.SampleDataMain -i customer_event -l /home/logs -o /home/sample_data/customer_event -s 100000 -id 100000

sleep 10 && echo "Waiting for 10 seconds before proceeding with next java programs"
java -cp data-integration-project.jar main.InsertFromDirectoryMain -d company -l /home/logs -t employee -i /home/sample_data/employee
java -cp data-integration-project.jar main.InsertFromDirectoryMain -d company -l /home/logs -t customer_event -i /home/sample_data/customer_event
