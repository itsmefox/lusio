package ch.viascom.lusio.business;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.DateTime;

import ch.viascom.base.exceptions.ServiceException;
import ch.viascom.base.exceptions.ServiceFault;
import ch.viascom.base.exceptions.ServiceResult;
import ch.viascom.base.exceptions.ServiceResultStatus;
import ch.viascom.base.utilities.IOUtils;
import ch.viascom.base.utilities.Stopwatch;
import ch.viascom.lusio.business.network.SimpleHttpClient;
import ch.viascom.lusio.module.GameModel;
import ch.viascom.lusio.module.SessionModel;
import ch.viascom.lusio.module.TipModel;
import ch.viascom.lusio.web.beans.LoginBean;

@SessionScoped
public class DataServiceProvider extends ServiceProvider implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Logger _logger = Logger.getLogger(DataServiceProvider.class.getName());

    private String _locationId = null;

    /**
     * Constructor.
     */
    public DataServiceProvider() {
        super();
    }

    /**
     * Gets a list with all available locations.
     * 
     * @throws ServerException
     * @throws Exception
     */
    public boolean logout() throws Exception {

        Stopwatch watch = Stopwatch.startNew();

        SimpleHttpClient client = getHttpClient("account/logout");
        String json = client.getHttpResponseString(client.getHttpGet());

        watch.stop();

        watch.restart();

        ServiceResult<String> result = getMapper().readValue(json, new TypeReference<ServiceResult<String>>() {
        });

        watch.stop();

        return result.getStatus() == ServiceResultStatus.successful ? true : false;
    }

    public String login(String username, String password) throws Exception {

        Stopwatch watch = Stopwatch.startNew();

        SimpleHttpClient client;
        client = getHttpClient("account/generateSession/?username={username}&password={password}", new BasicNameValuePair("username", username),
                new BasicNameValuePair("password", password));
        String json = client.getHttpResponseString(client.getHttpGet());
        _logger.info(json);
        watch.stop();

        watch.restart();
        try {
            ServiceResult<SessionModel> result = getMapper().readValue(json, new TypeReference<ServiceResult<SessionModel>>() {
            });
            if (result.getStatus() == ServiceResultStatus.successful) {
                return result.getContent().getSessionId();
            } else {
                ServiceResult<ServiceFault> resultException = getMapper().readValue(json, new TypeReference<ServiceResult<ServiceFault>>() {
                });
                throw new ServiceException(resultException.getContent().getCode(),resultException.getContent().getMessage());
            }
        } catch (Exception e) {
            ServiceResult<ServiceFault> resultException = getMapper().readValue(json, new TypeReference<ServiceResult<ServiceFault>>() {
            });

            throw new ServiceException(resultException.getContent().getCode(),resultException.getContent().getMessage());
        }

    }
    
    public List<GameModel> getLatestGames() throws Exception {

        Stopwatch watch = Stopwatch.startNew();

        SimpleHttpClient client;
        client = getHttpClient("game/latest");
        String json = client.getHttpResponseString(client.getHttpGet());
        _logger.info(json);
        watch.stop();

        watch.restart();
        try {
            ServiceResult<List<GameModel>> result = getMapper().readValue(json, new TypeReference<ServiceResult<List<GameModel>>>() {
            });
            if (result.getStatus() == ServiceResultStatus.successful) {
                return result.getContent();
            } else {
                ServiceResult<ServiceFault> resultException = getMapper().readValue(json, new TypeReference<ServiceResult<ServiceFault>>() {
                });
                throw new ServiceException(resultException.getContent().getCode(),resultException.getContent().getMessage());
            }
        } catch (Exception e) {
            ServiceResult<ServiceFault> resultException = getMapper().readValue(json, new TypeReference<ServiceResult<ServiceFault>>() {
            });

            throw new ServiceException(resultException.getContent().getCode(),resultException.getContent().getMessage());
        }

    }
    
    public List<TipModel> getLatestTips(String gameId) throws Exception {

        _logger.info("Load Tips");
        
        Stopwatch watch = Stopwatch.startNew();

        SimpleHttpClient client;
        client = getHttpClient("game/{gameId}/tips", new BasicNameValuePair("gameId", gameId));
        String json = client.getHttpResponseString(client.getHttpGet());
        _logger.info(json);
        watch.stop();

        watch.restart();
        try {
            ServiceResult<List<TipModel>> result = getMapper().readValue(json, new TypeReference<ServiceResult<List<TipModel>>>() {
            });
            if (result.getStatus() == ServiceResultStatus.successful) {
                return result.getContent();
            } else {
                ServiceResult<ServiceFault> resultException = getMapper().readValue(json, new TypeReference<ServiceResult<ServiceFault>>() {
                });
                throw new ServiceException(resultException.getContent().getCode(),resultException.getContent().getMessage());
            }
        } catch (Exception e) {
            ServiceResult<ServiceFault> resultException = getMapper().readValue(json, new TypeReference<ServiceResult<ServiceFault>>() {
            });

            throw new ServiceException(resultException.getContent().getCode(),resultException.getContent().getMessage());
        }

    }

    // /**
    // * Gets the location model for the given location ID. If no location with
    // * the ID is available, a new location with the ID as name is temporarily
    // * created.
    // *
    // * @param locationID
    // */
    // public LocationModel getLocationById(String locationID) {
    // try {
    // for (LocationModel location : getLocations()) {
    // if (location.getId().equals(locationID)) {
    // return location;
    // }
    // }
    // } catch (Exception e) {
    // throw new RuntimeException(e);
    // }
    //
    // return new LocationModel(locationID, locationID);
    // }
    //
    // /**
    // * Sets the greeting state of the given check-in to the given parameter
    // * greeted.
    // *
    // * @param checkInId
    // * ID of check-in (Column Checkin_ID in database)
    // * @param greeted
    // * Greeting state (true = greeted, false = not greeted)
    // * @throws Exception
    // *
    // * @returns Greeting date (can be null!)
    // */
    // public DateTime setGreetingState(String checkInId, boolean greeted)
    // throws Exception {
    // SimpleHttpClient client = getHttpClient("check-ins/setGreetingState");
    //
    // // Create POST request
    // HttpPost post = client.getHttpPost();
    // post.setHeader("Content-Type", "application/x-www-form-urlencoded");
    //
    // // POST form parameters (application/x-www-form-urlencoded)
    // List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
    // parameters.add(new BasicNameValuePair("checkInId", checkInId));
    // parameters.add(new BasicNameValuePair("greeted",
    // String.valueOf(greeted)));
    // post.setEntity(new UrlEncodedFormEntity(parameters));
    //
    // // Response is a DateTime-object with the greeting date/time.
    // // Greeting date/time is null if no greeted.
    // String json = client.getHttpResponseString(post);
    //
    // ServiceResult<DateTime> result = getMapper().readValue(json, new
    // TypeReference<ServiceResult<DateTime>>() {
    // });
    //
    // Log.i(LOG_TAG, "Set greeting state of Check-in " + checkInId + " to " +
    // greeted + " (" + result.getContent() + ")");
    //
    // return result.getContent();
    // }
    //
    // /**
    // * Gets a list of all check-ins (including the member) at the given
    // * location. If parameter fromCheckinTime is set, all check-ins at that
    // time
    // * and before are returned. The result is sorted ascending after the
    // * check-in time.
    // *
    // * @param fromCheckinTime
    // * Time from which check-ins are returned (must be today)
    // */
    // public List<CheckInModel> getCheckIns(DateTime fromCheckinTime) throws
    // Exception {
    // Stopwatch watch = Stopwatch.startNew();
    //
    // SimpleHttpClient client;
    //
    // if (fromCheckinTime != null) {
    // client = getHttpClient("check-ins/recent/{location}?from={from}", new
    // BasicNameValuePair("location", getLocationId()), new BasicNameValuePair(
    // "from", fromCheckinTime.toString(ModelConfig.DateTimeFormatter)));
    // } else {
    // client = getHttpClient("check-ins/recent/{location}", new
    // BasicNameValuePair("location", getLocationId()));
    // }
    //
    // HttpGet get = client.getHttpGet();
    // String json = client.getHttpResponseString(get);
    //
    // watch.stop();
    //
    // double loadingTime = watch.getElapsedSeconds() * 1000;
    //
    // ServiceResult<List<CheckInModel>> result = getMapper().readValue(json,
    // new TypeReference<ServiceResult<List<CheckInModel>>>() {
    // });
    //
    // if (result.getContent().size() > 0)
    // Log.i(LOG_TAG, String.format("%d checkin-members loaded in %.2f ms",
    // result.getContent().size(), loadingTime));
    // else
    // Log.d(LOG_TAG, String.format("%d checkin-members loaded in %.2f ms",
    // result.getContent().size(), loadingTime));
    //
    // return result.getContent();
    // }
    //
    // /**
    // * Loads the thumbnail for the given member.
    // *
    // * @param memberId
    // * Id of member (Kundennummer)
    // * @param width
    // * Width in pixels
    // * @param height
    // * Height in pixels
    // */
    // public ImageModel getMemberThumbnail(String memberId, int width, int
    // height) {
    // Stopwatch watch = Stopwatch.startNew();
    //
    // SimpleHttpClient client =
    // getHttpClient("/members/thumbnail/get/{memberId}?width={width}&height={height}",
    // new BasicNameValuePair("memberId", String.valueOf(memberId)), new
    // BasicNameValuePair("width", String.valueOf(width)), new
    // BasicNameValuePair(
    // "height", String.valueOf(height)));
    //
    // HttpGet get = client.getHttpGet();
    // HttpResponse response = null;
    // try {
    // response = client.getHttpResponse(get);
    // HttpEntity entity = response.getEntity();
    //
    // ImageModel image = null;
    //
    // String contentTypeString = entity.getContentType().getValue();
    // String type = contentTypeString.toLowerCase().replace("image/", "");
    //
    // InputStream content;
    // content = entity.getContent();
    //
    // ByteArrayOutputStream imageStream =
    // IOUtils.toByteArrayOutputStream(content, true);
    //
    // if (imageStream != null && imageStream.size() > 0) {
    // byte[] binaryContent = imageStream.toByteArray();
    // image = new ImageModel(binaryContent, ImageModel.getType(type));
    // }
    //
    // watch.stop();
    //
    // Log.v(LOG_TAG, String.format("Image loaded in %.2f ms",
    // watch.getElapsedMilliseconds()));
    //
    // return image;
    // } catch (HttpResponseException httpEx) {
    // watch.stop();
    //
    // if (httpEx.getStatusCode() != 404) {
    // Log.e(LOG_TAG, "Unable to load image for member id: " + memberId +
    // " - status code: " + httpEx.getStatusCode(), httpEx);
    //
    // ACRA.getErrorReporter().handleException(httpEx);
    // }
    //
    // Log.d(LOG_TAG, "No image for member id: " + memberId +
    // " found (request took " + watch.getElapsedMilliseconds() + " ms)");
    // } catch (Exception ex) {
    // Log.e(LOG_TAG, "Failed to load member thumbnail.", ex);
    //
    // ACRA.getErrorReporter().handleException(ex);
    // }
    //
    // return null;
    // }
    //
    // public boolean setMemberThumbnail(String memberId, Bitmap image) {
    // SimpleHttpClient client = getHttpClient("thumbnail/save/{memberId}", new
    // BasicNameValuePair("memberId", String.valueOf(memberId)));
    //
    // ByteArrayOutputStream bos = new ByteArrayOutputStream();
    //
    // image.compress(CompressFormat.JPEG, 80, bos);
    //
    // byte[] data = bos.toByteArray();
    //
    // // Create POST request
    // HttpPost post = client.getHttpPost();
    // post.setHeader("Content-Type", "application/octet-stream");
    //
    // // POST form parameters (application/x-www-form-urlencoded)
    // post.setEntity(new ByteArrayEntity(data));
    //
    // ServiceResult<Object> result = null;
    // // Response is nothing
    // String json;
    // try {
    // json = client.getHttpResponseString(null);
    //
    // result = getMapper().readValue(json, new
    // TypeReference<ServiceResult<Object>>() {
    // });
    // Log.i(LOG_TAG, "Saving image was " + result.getStatus());
    // return (ServiceResultStatus.successful.equals(result.getStatus())) ? true
    // : false;
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // return false;
    // }
    //
    // public String generateCheckIn(String badgeNumber, String location) throws
    // Exception {
    // SimpleHttpClient client = getHttpClient("check-ins/create");
    //
    // // Create POST request
    // HttpPost post = client.getHttpPost();
    // post.setHeader("Content-Type", "application/x-www-form-urlencoded");
    //
    // // POST form parameters (application/x-www-form-urlencoded)
    // List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
    // parameters.add(new BasicNameValuePair("badgeNumber", badgeNumber));
    // parameters.add(new BasicNameValuePair("location", location));
    // post.setEntity(new UrlEncodedFormEntity(parameters));
    //
    // // Response is a DateTime-object with the greeting date/time.
    // // Greeting date/time is null if no greeted.
    // String json = client.getHttpResponseString(post);
    //
    // ServiceResult<String> result = getMapper().readValue(json, new
    // TypeReference<ServiceResult<String>>() {
    // });
    //
    // Log.i(LOG_TAG, "Generate check-in for " + badgeNumber + " at " +
    // location);
    //
    // return result.getContent();
    // }
    //
    // public List<MemberModel> getListOfMembers(String searchString) throws
    // Exception {
    //
    // Stopwatch watch = Stopwatch.startNew();
    //
    // SimpleHttpClient client;
    //
    // // if (searchString != null) {
    // client = getHttpClient("members/list?s={searchString}", new
    // BasicNameValuePair("searchString", searchString));
    // // } else {
    // // TODO: was passiert wenn kein such String übergeben wird?
    // // }
    //
    // HttpGet get = client.getHttpGet();
    // String json = client.getHttpResponseString(get);
    //
    // watch.stop();
    //
    // double loadingTime = watch.getElapsedSeconds() * 1000;
    //
    // ServiceResult<List<MemberModel>> result = getMapper().readValue(json, new
    // TypeReference<ServiceResult<List<MemberModel>>>() {
    // });
    //
    // if (result.getContent().size() > 0)
    // Log.i(LOG_TAG, String.format("%d members loaded in %.2f ms",
    // result.getContent().size(), loadingTime));
    // else
    // Log.d(LOG_TAG, String.format("%d members loaded in %.2f ms",
    // result.getContent().size(), loadingTime));
    //
    // return result.getContent();
    //
    // }
}
