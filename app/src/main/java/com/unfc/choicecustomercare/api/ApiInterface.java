package com.unfc.choicecustomercare.api;

import com.google.gson.JsonObject;
import com.unfc.choicecustomercare.models.BaseEntity;
import com.unfc.choicecustomercare.models.HistoryEntity;
import com.unfc.choicecustomercare.models.MessageEntity;
import com.unfc.choicecustomercare.models.ResponderEntity;
import com.unfc.choicecustomercare.models.Room;
import com.unfc.choicecustomercare.models.UpdateResponseModel;
import com.unfc.choicecustomercare.models.UserEntity;
import com.unfc.choicecustomercare.models.UserInfoEntity;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Hai Nguyen - 8/27/15.
 */
public interface ApiInterface{

	@POST("/api/account/register")
	void doRegister(@Body UserEntity user, Callback<BaseEntity> callback);

	@FormUrlEncoded
	@POST("/token")
	void doLogIn(@Field("Username") String userName, @Field("Password") String password,
			@Field("grant_type") String type, @Field("DeviceId") String deviceId, Callback<UserInfoEntity> callback);

    @FormUrlEncoded
    @POST("/api/Account/Update")
    void doUpdate(@Field("FirstName") String FirstName,
                  @Field("LastName") String LastName,
                  @Field("ResponderId") String ResponderId,
                  @Field("Bio") String Bio,
                  @Field("ProfilePhoto") String ProfilePhoto,
                  @Field("IsChargedNurse") String IsChargedNurse,

                 Callback<UpdateResponseModel> callback);

	@GET("/api/message")
	void getMessages(Callback<MessageEntity> callback);

	@GET("/api/message/responders")
	void getResponders(Callback<List<ResponderEntity>> callback);

	@POST("/api/message/accept")
	void doAcceptMessage(@Query("messageId") String messageId, @Query("messageTypeid") String messageTypeId,@Query("responderId") String responderId,
			Callback<BaseEntity> callback);

	@POST("/api/message/deny")
	void doDenyMessage(@Query("messageId") String messageId, @Query("id") int id, Callback<BaseEntity> callback);

	@POST("/api/account/logout")
	void doLogOut(Callback<BaseEntity> callback);

	@GET("/api/history")
	void getMessageHistory(Callback<List<HistoryEntity>> callback);

	@POST("/api/account/changepassword")
	void doChangePassword(@Body JsonObject body, Callback<List<MessageEntity>> callback);

	@POST("/api/message/roomrequest")
	void getRequestRoom(@Query("id") int roomId, Callback<List<MessageEntity>> callback);

	@POST("/api/message/responderrequest")
	void getResponderRequest(@Query("id") int responderId, Callback<List<ResponderEntity>> callback);

	@GET("/api/responderroom/getassignedrooms")
	void getAssignedRooms(@Query("responderId") int responderId, Callback<List<Room>> callback);

	@GET("/api/responderroom/getunassignedrooms")
	void getUnAssignedRooms(@Query("responderId") int responderId, Callback<List<Room>> callback);

	@POST("/api/responderroom/unassignroom")
	void setUnassignRoom(@Query("roomId") int roomId, Callback<BaseEntity> callback);


}
