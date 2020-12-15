package com.company.rmis;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import com.company.impls.*;

import javax.swing.*;

public class SpecialRmi {

    public static void main(String[] args) {
        try {
            Registry registry = java.rmi.registry.LocateRegistry.createRegistry(4000);
            SpecialImpl special = new SpecialImpl();
            registry.rebind("Special", special);
        } catch (RemoteException exception) {
            JOptionPane.showMessageDialog(null, "Server failure: " + exception.getMessage());
        }
    }

}
