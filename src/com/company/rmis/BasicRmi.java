package com.company.rmis;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import com.company.impls.*;

import javax.swing.*;

public class BasicRmi {

    public static void main(String[] args) {
        try {
            Registry registry = java.rmi.registry.LocateRegistry.createRegistry(3000);
            BasicImpl basic = new BasicImpl();
            registry.rebind("Basic", basic);
        } catch (RemoteException exception) {
            JOptionPane.showMessageDialog(null, "Server failure: " + exception.getMessage());
        }
    }

}
