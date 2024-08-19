# WorkoutTimer
This is a hobbyproject for displaying different exercises with timer and description during a workout.
It is implemented in kotlin using TornadoFX and might be updated from time to time. Or maybe it wont. Lets see.

> [!IMPORTANT]
Place a dadum.wav into the data/ folder

#### Use .json files
You can load your own workouts by using JSON files, placed inside the data/ folder:
```json
{
  "name": "Name of some Exercise"
  "duration": Duration of Exercise in Seconds
  "description" The thing where you move your body with your arms.
  "image": pullupImage.png
  "next": {
    {
      "name": "Pullups"
      "duration": 60
      "next": null
    }
  }
}
```
The name of the file will be displayed in the workout selection.
The duration specifies the duration of that segmet in seconds.
The description provides a string that can be shown during the section in smaller font.
The image determines a filepath for an image that cam be shown during the section. 
This path is specified relative to the data/ folder.
The description as well as the image might be omitted.

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

#### Use .schedule file
In addition to the .json format, there is a new file format that is supposed to provide
a more practical method to specify workouts.

```schedule
# Pullups
duration: 60
description: Yeah, I don't know what they are either.
image: pullupImage.png

## repeat 3 {
  # Pushups
  description: lying on the ground (I think)
  duration: 30
}
```

In this format, the name is specified after one "#" while all the other
fields are specified by the fieldname followed by ":".

Repeat-blocks start specified by "## repeat" followed by the repetition number and "{"
and end with "}"

The "next" field doesn't exist anymore, the order is specified by the order inside the file.
Also, every field or line, I talked about above (also "}") must be inside a seperate line.
Apart from that, all the rules from the .json format apply here. 

