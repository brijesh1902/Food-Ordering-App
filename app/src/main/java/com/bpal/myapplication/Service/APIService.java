package com.bpal.myapplication.Service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
        {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA1NpO0Fc:APA91bHGTzkg5Or--rT7yibOtJoIRJHYReBlNpDjAe0gLFzzXVWfwzn2rKcVZVNP2NYjNLDlB1UEelM_PVyjD4hWb5bkj5nwrkoeSdKkYlYUxRlbArc-fbllW0Mm_YX7AroBCyEbZEjh"
        }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
