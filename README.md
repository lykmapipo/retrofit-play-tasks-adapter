retrofit-play-tasks-adapter
===========================

[![](https://jitpack.io/v/lykmapipo/retrofit-play-tasks-adapter.svg)](https://jitpack.io/#lykmapipo/retrofit-play-tasks-adapter)

A [retrofit](https://github.com/square/retrofit) call adapter for adapting [play service tasks](https://developers.google.com/android/guides/tasks).

## Installation
Add [https://jitpack.io](https://jitpack.io) to your build.gradle with:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
Add `retrofit-play-tasks-adapter` dependency into your project

```gradle
dependencies {
    implementation 'com.github.lykmapipo:retrofit-play-tasks-adapter:v0.1.3'
}
```

## Usage

Add `TaskCallAdapterFactory` as a `Call` adapter when building your `Retrofit` instance:
```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.example.com/v1")
    .addCallAdapterFactory(TaskCallAdapterFactory.create())
    .build();
```

Your service methods can now use `Task` as their return type.
```java
interface MyService {
  @GET("/user/me")
  Task<User> getMe();
};

MyService api = retrofit.create(MyService.class);

Task<User> task = api.getMe();
task.addOnSuccessListener(new OnSuccessListener<User>() {
         @Override
         public void onSuccess(User user) {
             // Task completed successfully
             // ...
         }
     });
task.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            // Task failed with an exception
            // ...
        }
    });
```

## Test
```sh
./gradlew test
```

## Contribute
It will be nice, if you open an issue first so that we can know what is going on, then, fork this repo and push in your ideas.
Do not forget to add a bit of test(s) of what value you adding.

## License

(The MIT License)

Copyright (c) lykmapipo && Contributors

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
'Software'), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
