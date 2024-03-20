import os

files = os.listdir("./res/executions")
new_file = "all_tables.sql"

with open("./res/executions/" + new_file, "w") as f:
    for file in files:
        with open("./res/executions/" + file) as f2:
            lines = f2.readlines()
            
            for i in range(len(lines)):
                if "CREATE TABLE" in lines[i]:
                    f.write(lines[i])
                    i += 1
                    
                    while ");" not in lines[i]:
                        f.write(lines[i])
                        i += 1
                        
                    f.write(lines[i])
                    f.write("\n")
                    break
                        