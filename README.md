#Akka message delivery relibility
## 1. at-most-once
### Start Teacher

```batch
sbt "run-main school.atmostonce.MathApp teacher"
```

### Start Student

```batch
sbt "run-main school.atmostonce.MathApp student"
```

### Start Teacher and Student

```batch
sbt "run-main school.atmostonce.MathApp"
```

## 2. akka persistence
### Start Teacher

```batch
sbt "run-main school.akkapersistence.MathApp teacher"
```

### Start Student

```batch
sbt "run-main school.akkapersistence.MathApp student"
```

### Start Teacher and Student

```batch
sbt "run-main school.akkapersistence.MathApp"
```

## 3. at-least-once
### Start Teacher

```batch
sbt "run-main school.atleastonce.MathApp teacher"
```

### Start Student

```batch
sbt "run-main school.atleastonce.MathApp student"
```

### Start Teacher and Student

```batch
sbt "run-main school.atleastonce.MathApp"
```

## 4. exactly-once
### Start Teacher

```batch
sbt "run-main school.exactlyonce.MathApp teacher"
```

### Start Student

```batch
sbt "run-main school.exactlyonce.MathApp student"
```

### Start Teacher and Student

```batch
sbt "run-main school.exactlyonce.MathApp"
```

## References
* [AKKA REMOTE SAMPLES WITH SCALA](http://www.typesafe.com/activator/template/akka-sample-remote-scala)
* [AKKA PERSISTENCE SAMPLES WITH SCALA](http://www.typesafe.com/activator/template/akka-sample-persistence-scala)
