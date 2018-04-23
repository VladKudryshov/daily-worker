import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;


public class DialyWorker {
    private static DatabaseReference database;
    private static Map<String, List<ProductInfoModel>> lists = new HashMap<>();

    public static void main(String[] args) throws Exception {

        FileInputStream serviceAccount =
                new FileInputStream("shooplists-firebase-adminsdk-8k2xu-4073c0330e.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://shooplists.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);

        database = FirebaseDatabase.getInstance().getReference();

        while (true) {
            try {
                getUsers();
                Thread.sleep(1000 * 10);
                Map<String, List<ProductInfoModel>> smartLists = new HashMap<>();
                for (Map.Entry<String, List<ProductInfoModel>> user : lists.entrySet()) {
                    Map<String, List<Long>> collect = user.getValue()
                            .stream()
                            .collect(Collectors.toMap(item -> item.getName(),
                                    item -> user
                                            .getValue()
                                            .stream()
                                            .filter(f -> f.getName().equals(item.getName()))
                                            .filter(f -> f.getDatePaid() != null)
                                            .map(f -> f.getDatePaid())
                                            .sorted(Comparator.reverseOrder())
                                            .collect(Collectors.toList()),
                                    (c1, c2) -> c1));
                    Map<String, Double> items = new HashMap<>();
                    for (Map.Entry<String, List<Long>> item : collect.entrySet()) {
                        List<Long> peroiod = new ArrayList<>();
                        List<Long> value = item.getValue();
                        for (int i = 0; i < value.size() - 1; i++) {
                            peroiod.add(Math.round((double) (value.get(i) - value.get(i + 1)) / (1000 * 60 * 60 * 24)));
                        }
                        peroiod.stream().mapToDouble(a -> a).average().ifPresent(f -> items.put(item.getKey(), Math.ceil(f)));
                    }
                    List<ProductInfoModel> productInfoModels = new ArrayList<>();
                    Calendar calendar = new GregorianCalendar();
                    Calendar now = new GregorianCalendar();
                    items.forEach((String key, Double value) -> {
                        calendar.setTimeInMillis(collect.get(key).get(0));
                        int oldDay = calendar.get(Calendar.DAY_OF_YEAR);
                        int nowDay = now.get(Calendar.DAY_OF_YEAR);
                        if ((nowDay - oldDay) == value.intValue() && oldDay != nowDay) {
                            ProductInfoModel productInfoModel = new ProductInfoModel();
                            productInfoModel.setName(key);
                            productInfoModel.setPaid(false);
                            productInfoModel.setQuantity(1);
                            productInfoModels.add(productInfoModel);
                        }
                    });
                    smartLists.put(user.getKey(), productInfoModels);
                }

                smartLists.forEach((uid,list)->{
                    String docketsInfoUid = database.child("docketsInfo").push().getKey();
                    String smartListsUid = database.child("smartLists").push().getKey();

                    int quantity = list.stream().mapToInt(item -> item.getQuantity()).sum();

                    database.child("docketsInfo").child(docketsInfoUid).setValue(list);
                    SmartListModel model = new SmartListModel();
                    model.setDocketName("Smart List");
                    model.setUid(smartListsUid);
                    model.setDocketInfoUid(docketsInfoUid);
                    model.setQuantity(quantity);
                    HashMap<String, Boolean> user = new HashMap<>();
                    user.put(uid,true);
                    model.setUser(user);
                    database.child("smartList").child(smartListsUid).setValue(model);
                });

                Thread.sleep(1000 * 60 * 60 * 24);
            } catch (InterruptedException ignored) {
            }
        }

    }

    private static void getUsers() {
        database.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        database.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Map<String, Object> valueUser = (Map<String, Object>) data.getValue();
                    database.child("dockets").orderByChild("user/" + valueUser.get("uid")).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Map<String, Object> valueDockets = (Map<String, Object>) data.getValue();
                                database.child("docketsInfo").child(String.valueOf(valueDockets.get("docket_info_uid"))).child("products").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        GenericTypeIndicator<List<ProductInfoModel>> genericTypeIndicator = new GenericTypeIndicator<List<ProductInfoModel>>() {
                                        };
                                        if (lists.containsKey(String.valueOf(valueUser.get("uid")))){
                                            lists.get(String.valueOf(valueUser.get("uid"))).addAll(dataSnapshot.getValue(genericTypeIndicator));
                                        }else {
                                            lists.put(String.valueOf(valueUser.get("uid")),dataSnapshot.getValue(genericTypeIndicator));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
