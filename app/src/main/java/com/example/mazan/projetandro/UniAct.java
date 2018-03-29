package com.example.mazan.projetandro;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.TextView;

import static java.lang.Thread.sleep;

public class UniAct extends AppCompatActivity {

    private Thread t;
    private boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uni);

        // ACTIVER LE BLUETOOTH DE FORCE A L'ENTREE DE L'APPLI
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        //RENOMMER LA DEVICE BLUETOOTH
        Button rename = (Button) (findViewById(R.id.rename));
        rename.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          final BluetoothAdapter myBTAdapter = BluetoothAdapter.getDefaultAdapter();
                                          final long lTimeToGiveUp_ms = System.currentTimeMillis() + 10000;

                                          //ON DEFINIT LE NVO NOM DE LA DEVICE
                                          EditText insertid = (EditText) (findViewById(R.id.insertId));
                                          final String sNewName = insertid.getText().toString();
                                          Log.i("1", sNewName);

                                          if (myBTAdapter != null)
                                          {
                                              String sOldName = myBTAdapter.getName();
                                              if (sOldName.equalsIgnoreCase(sNewName) == false)
                                              {
                                                  final Handler myTimerHandler = new Handler();
                                                  myBTAdapter.enable();
                                                  myTimerHandler.postDelayed(
                                                          new Runnable()
                                                          {
                                                              @Override
                                                              public void run()
                                                              {
                                                                  if (myBTAdapter.isEnabled())
                                                                  {
                                                                      myBTAdapter.setName(sNewName);
                                                                      if (sNewName.equalsIgnoreCase(myBTAdapter.getName()))
                                                                      {
                                                                          Log.i("New Name", "Updated BT Name to " + myBTAdapter.getName());
                                                                          myBTAdapter.disable();
                                                                      }
                                                                  }
                                                                  if ((sNewName.equalsIgnoreCase(myBTAdapter.getName()) == false) && (System.currentTimeMillis() < lTimeToGiveUp_ms))
                                                                  {
                                                                      myTimerHandler.postDelayed(this, 500);
                                                                      if (myBTAdapter.isEnabled())
                                                                          Log.i("10", "Update BT Name: waiting on BT Enable");
                                                                      else
                                                                          Log.i("11", "Update BT Name: waiting for Name (" + sNewName + ") to set in");
                                                                  }
                                                              }
                                                          } , 500);
                                              }
                                          }
                                      }
                                  });


        // ENREGISTRER ET PASSER EN "STRING" LA VALEUR DE  L'ID UTILISATEUR lors d'un appui sur le bouton "lancer"
        Button lancer = (Button) (findViewById(R.id.lancer));
        lancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                // THREAD PERMETTANT LA DECOUVERTE DU MOBILE VIA BLUETOOTH POSSIBLE EN CONTINUE
                t = new Thread(
                        new Runnable() {
                            public void run() {
                                while (running) {
                                    //ACTIVER LA FONCTION DISCOVERY
                                    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300); // peut être modifier la value en seconde au lieu de relancer le thread ?
                                    startActivity(discoverableIntent);
                                    try {
                                        sleep(299999);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                );
                t.start();

            }
        });
    }

}

/*
                //METHODE POUR CHANGER LE NOM
                void ChangeDeviceName(){
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    Log.i("Ancien nom", "localdevicename : " + bluetoothAdapter.getName() + " localdeviceAddress : " + bluetoothAdapter.getAddress());
                    bluetoothAdapter.setName(data);
                    Log.i("Nouveau nom", "localdevicename : " + bluetoothAdapter.getName() + " localdeviceAddress : " + bluetoothAdapter.getAddress());
                }
                */