package com.company.impls;

import com.company.interfaces.BasicCalculator;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BasicImpl extends UnicastRemoteObject implements BasicCalculator{

    public BasicImpl() throws RemoteException {
        double numberOne, numberTwo;
    }

    @Override
    public double div(double numberOne, double numberTwo) throws RemoteException {
        if (numberTwo == Double.parseDouble("0")) {
            return 0;
        }
        return numberOne / numberTwo;
    }

    @Override
    public double multi(double numberOne, double numberTwo) throws RemoteException {
        return numberOne * numberTwo;
    }

    @Override
    public double plus(double numberOne, double numberTwo) throws RemoteException {
        return numberOne + numberTwo;
    }

    @Override
    public double minus(double numberOne, double numberTwo) throws RemoteException {
        return numberOne - numberTwo;
    }
}
