You can find the task and methods which were created to cover the task below.

1. Check that API return valid responses: values are not empty, values have valid type.
- testGeneralJokeById_Validation()
- testRandomJoke_Validation()
- testKnockKnockJoke_Validation()
- testProgrammingJoke_Validation()

 
2. Joke ids are linked to the jokes and can't be changed.
- testJokeById_LinkedToJoke()
- testRandomJoke_LinkedToJoke()
- testKnockKnockJoke_LinkedToJoke()
- testProgrammingJoke_LinkedToJoke()
- testJokeById_AttemptToChangeId()

3. API should returns human readable error if inexistent joke is requested.
- testErrorForNonExistentJoke()

4. API should really return a random joke if a user uses API 'random' feature.
- testRandomJoke_ReturnsDifferentJokes()
