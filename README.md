# Starting Application #
You will need to make sure you have mysql install..look in ```application.properties```

Go to the application root and type: **gradle bootRun**

# Running Tests #

### Unit and Integration Test ###
The integration test are done using an embdeded database

**gradle test**

### Load and Performance Test ###
Gatlin is used for load and performance testing.

##### Load ####

You will need to modify the class ```com.vote.CountUpSimulation``` to specify
the load. Then run:

**gradle gatlinLoad**

##### Performance ####
You will need to modify the following scala class:
```com.vote.VoteCountResultSimulation``` with the appropriate time ranges
that you want to check the performance with the new load generated.

**gradle gatlinCount**


# Usage #

1: To register vote for a candidate: You need to create
a ***POST*** request:

```/countMeUp/candidate/{candidateId}}/voter/{voterId}```

***candidateId***: The id of the candidate

***voterId***: The id of the voter

2:
To get the count of votes between dates: You need to create
a ***GET*** request to:

```/countMeUp/results/start/{startDateTime}/end/{endDateTime}```

***startDateTime***: The start date to query from

***endDateTime***: The end date to query from

The date should be in the following formats:
yyyy-MM-dd HH:mm:ss