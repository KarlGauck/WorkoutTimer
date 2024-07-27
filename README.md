# WorkoutTimer
This is a hobbyproject for displaying different exercises with timer and description during a workout.
It is implemented in kotlin using TornadoFX and might be updated from time to time. Or maybe it wont. Lets see.

> [!IMPORTANT]
Place a dadum.wav into the data/ folder

#### Use files
You can load your own workouts by using JSON files, placed inside the data/ folder:
```json
{
  "name": "Name of some Exercise"
  "duration": Duration of Exercise in Seconds
  "next": {
    {
      "name": "Some name"
      "duration": 3
      "next": null
    }
  }
}
```
The name of the file will be displayed in the workout selection.

You can also specify repetition routines:
```json
{
  "repetitions": 5
  "elements": {
    {
      "name": ...
      ...
    },
    {
      ...
    }
  }
}
```
Those repetitions can be used everywhere, an exercise can be used.
