# json-scrubber
Micro services to scrub sensitive information in json payload based on REST service

I take this branch as a practice of version management, once this personal interest branch has passed integration test, this branch should merge in the master branch.

Since there is no sample input provided, I just assume "email", "first name", "password" and "phone number" are sensitive information fields that should go in the log file.

Busides, I also added UUID and local zoned datetime as the metadata of scrubbed sensitive information to notify when and where it has been logged.

Too make it convenient, I just output the log file in place.

I will check if input is valid for every field, otherwise you will receive an error which indicates the specific field.

Github informed me that there might be a 3rd-party dependency which is vulnerable. Please ignore that because it is an edge version of that dependency, I'm not using that in my implementation. I'll just remove for security purpose, because it just informed me right before 4pm.

Also there is a list version for json scrubber which handles multiple inputs: https://github.com/LingboTang/json-scrubber/tree/Add_List_Of_Data.
