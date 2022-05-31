package edu.uw.tcss450.group8.chatapp.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group8.chatapp.databinding.FragmentHomeBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.ChatroomViewModel;
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.ContactListViewModel;
import edu.uw.tcss450.group8.chatapp.ui.location.Location;
import edu.uw.tcss450.group8.chatapp.ui.location.LocationViewModel;
import edu.uw.tcss450.group8.chatapp.ui.weather.Weather;
import edu.uw.tcss450.group8.chatapp.ui.weather.WeatherHourlyRecyclerViewAdapter;
import edu.uw.tcss450.group8.chatapp.ui.weather.WeatherViewModel;


/**
 * Class for user home page
 * Adapted from original code by Charles Bryan
 *
 * @author Charles Bryan
 * @author Shilnara Dam
 * @author Sean Logan
 * @version 5/29/22
 */
public class HomeFragment extends Fragment {

    private WeatherViewModel mWeatherModel;
    private FragmentHomeBinding mBinding;
    private ChatroomViewModel mChatroomModel;
    private ContactListViewModel mContactListModel;
    private UserInfoViewModel mUser;
    private LocationViewModel mLocation;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mContactListModel = new ViewModelProvider(requireActivity()).get(ContactListViewModel.class);

        mWeatherModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);

        mChatroomModel = new ViewModelProvider(requireActivity()).get(ChatroomViewModel.class);
        mChatroomModel.getChatRoomsForUser(mUser.getJwt());

        mContactListModel = new ViewModelProvider(requireActivity()).get(ContactListViewModel.class);
        mContactListModel.getContacts(mUser.getJwt());

        mLocation = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mBinding = FragmentHomeBinding.inflate(inflater);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWeatherModel.addCurrentWeatherObserver(
                getViewLifecycleOwner(),
                this::observeCurrentWeatherResponse);
        mWeatherModel.addHourlyWeatherObserver(this,
                weatherList ->
                        mBinding.listWeatherHourly.setAdapter(new WeatherHourlyRecyclerViewAdapter(weatherList)));

        mContactListModel.addContactsListObserver(
                getViewLifecycleOwner(),
                contacts -> {
                    mBinding.listContactsHomeFragment.setAdapter(
                            new HomeContactViewRecyclerAdapter(contacts, this)
                    );
                });

        mChatroomModel.addChatRoomListObserver(
                getViewLifecycleOwner(),
                chatroomList -> {
                    mBinding.listChatroomHomeFragment.setAdapter(
                            new HomeChatroomViewRecyclerAdapter(chatroomList, this)
                    );

        });

        mLocation.addLocationObserver(getViewLifecycleOwner(), location -> {
            mWeatherModel.getWeatherLatLon(String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude()));
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Observer for current weather
     *
     * @param theWeather Weather
     */
    private void observeCurrentWeatherResponse(final Weather theWeather) {
        mBinding.textCurrentCondition.setText(theWeather.getCondition());
    }

    /**
     * open chatroom with the desired contact
     *
     * @param email String email of the friend
     */
    public void homeSendMessage(String email) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
    }

    /**
     * unfriend a contact
     *
     * @param email String email of the friend
     */
    public void homeUnFriend(String email) {
        mContactListModel.unfriend(mUser.getJwt(), email);
    }

    /**
     * Enters a chat room with a contact.
     *
     * @param chatId int
     */
    public void homeStartChat(int chatId, String chatName) {
        Navigation.findNavController(getView()).
                navigate(HomeFragmentDirections.
                        actionNavHomeFragmentToMessageListFragment(chatName, chatId));

    }
}
