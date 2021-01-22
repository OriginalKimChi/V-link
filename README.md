# V-link

V-link is video-commerce service using interective elements in video

-   Use Srping boot 2.3.4
-   Use gradle build
-   Use JPA
-   Use MongoDB(with Mongo atlas)
-   Use Mongo session
-   Use Thyemleaf
-   Use AWS(EC2, S3)

## Requirements

For building and running the application you need:

-   JDK 1.8
-   Gradle 6
  
## User Stories

-   Sign up(with email authentication)
-   Login/Logout
-   Update User profile
-   Find password(with email authentication)
-   See hot video(By viewCount) *only show top 3 ranked video
-   See hot product(By viewCount) *only show top 5 ranked product
-   See video with hashtag
-   See video detail with included product
-   See product detail(this info is connected with Original product link in Himart)
-   Video time jump to product appearance section
-   Like Video
-   Save video in My-page
-   Add comment in video

## Partners Stories 

-   Register Partners(with authentication)
-   Login/Logout
-   Update Partners profile
-   Find password
-   Register Video(Upload video, add product in video)
-   Update/Delete Video
-   Get product info from "Himart", using jsoup(data scraping)
-   See dashboard(Video statics)
-   See own video rank(By viewCount) 
-   See own product rank(By viewCount)

## Admin Stories

-   See all partners include info
-   See partners who didn't authenticated
-   Authenticate partners

## User screen

### Main feed

![1](https://user-images.githubusercontent.com/48902646/105429877-ad299c80-5c95-11eb-9ff4-5dc838c79189.png)

### Video detail 

![2](https://user-images.githubusercontent.com/48902646/105429943-d1857900-5c95-11eb-97fb-660b52195787.png)

### See Product detail in video detail view 

![4 수정](https://user-images.githubusercontent.com/48902646/105430034-f679ec00-5c95-11eb-9d7a-4ada02a377e7.png)

### Move to product original purchase link

![8](https://user-images.githubusercontent.com/48902646/105430109-21fcd680-5c96-11eb-8ae6-60b803a1a605.png)

### See hashtag video

![5](https://user-images.githubusercontent.com/48902646/105430151-3a6cf100-5c96-11eb-9d27-ef683e5671cd.png)

### See saved video in My-page

![7](https://user-images.githubusercontent.com/48902646/105430191-4e185780-5c96-11eb-8f3e-5b3540ac1b58.png)


## License

Released under the Apache License 2.0. See the LICENSE file.
