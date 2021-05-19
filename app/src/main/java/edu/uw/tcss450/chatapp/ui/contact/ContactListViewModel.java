package edu.uw.tcss450.chatapp.ui.contact;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.chatapp.io.RequestQueueSingleton;

public class ContactListViewModel extends AndroidViewModel {
    /**Initializer for contact list mutable live data**/
    private MutableLiveData<List<Contact>> mFriendContactList;
    /**Initializer for contact search list mutable live data**/
    private MutableLiveData<List<Contact>> mSearchContacts;
    /**Initializer for JSONobject live data**/
    private final MutableLiveData<JSONObject> mResponse;

    /**
     * Constructor for Contact List View Model
     * @param application the application
     */
    public ContactListViewModel(@NonNull Application application) {
        super(application);
        //Sample contact list generator
        //ContactGenerator generator = new ContactGenerator();//
        //mContactList = new MutableLiveData<>(generator.getContactList());
        //mContactListFull = new MutableLiveData<>(generator.getContactList());
        mFriendContactList = new MutableLiveData<>(new ArrayList<>());
        mSearchContacts = new MutableLiveData<>(new ArrayList<>());
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }


    /**
     * contact friend list observer
     * @param owner life cycle owner
     * @param observer observer for list<Contact>
     */
    public void addContactFriendListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<Contact>> observer) {
        mFriendContactList.observe(owner, observer);
    }

    /**
     * Add contact Search list observer
     * @param owner life cycle owner
     * @param observer observer for list<Contact>
     */
    public void addContactSearchObserver(@NonNull LifecycleOwner owner,
                                          @NonNull Observer<? super List<Contact>> observer) {
        mSearchContacts.observe(owner, observer);
    }

    /**
     * Request to get contact friend list from backend
     * @param jwt authorization token
     */
    public void connectContactFriendList(String jwt) {
        String url = "https://group1-tcss450-project.herokuapp.com/contacts";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleSuccess,
                this::handleError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * method to handle success contactFriendList feedback from backend
     * @param result json result object for friend contact list
     */
    private void handleSuccess(final JSONObject result) {
        ArrayList<Contact> temp = new ArrayList<>();
        try {
            JSONArray contacts = result.getJSONArray("contacts");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject contact = contacts.getJSONObject(i);
                int verified = contact.getInt("verified");
                if (verified == 1) {
                    String firstName = contact.getString("firstName");
                    String lastName = contact.getString("lastName");
                    String email = contact.getString("email");
                    String username = contact.getString("userName");
                    int memberID = contact.getInt("memberId");
                    Contact entry = new Contact(firstName, lastName, email, username, memberID);
                    temp.add(entry);
                }
            }
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        mFriendContactList.setValue(temp);
    }

    /**
     * Request to get contact search list from backend
     * @param jwt authorization token
     */
    public void connectContactSearchList(String jwt) {
        String url = "https://group1-tcss450-project.herokuapp.com/contacts/search";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleSuccessSearch,
                this::handleError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * method to handle success search feedback for backend
     * @param result json object for search contact list
     */
    private void handleSuccessSearch(JSONObject result) {
        ArrayList<Contact> searchResult = new ArrayList<>();
        try {
            JSONArray contacts = result.getJSONArray("contacts");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject contact = contacts.getJSONObject(i);
                String firstName = contact.getString("firstName");
                String lastName = contact.getString("lastName");
                String email = contact.getString("email");
                String username = contact.getString("userName");
                int memberID = contact.getInt("memberId");
                Contact entry = new Contact(firstName, lastName, email, username, memberID);
                searchResult.add(entry);
            }
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        mSearchContacts.setValue(searchResult);
    }

    /**
     * method to handleError for backend
     * @param error Volley Error.
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                JSONObject response = new JSONObject();
                response.put("code", error.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
                mResponse.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    /**
     * Getter for contact list
     * @return search contact list values
     */
    public List<Contact> getList() {
        return this.mSearchContacts.getValue();
    }

}