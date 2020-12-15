package com.company.impls;

import com.company.interfaces.SpecialCalculator;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SpecialImpl extends UnicastRemoteObject implements SpecialCalculator {

    public SpecialImpl() throws RemoteException {
        double numberOne, numberTwo;
    }

    @Override
    public double perCent(double numberOne, double numberTwo) throws RemoteException {
        return (numberTwo * numberOne) / 100;
    }

    @Override
    public double elevation(double numberOne, double numberTwo) throws RemoteException {
        if (numberTwo < Double.parseDouble("0")) {
            return 0;
        }

        return Math.pow(numberOne, numberTwo);
    }

    @Override
    public double root(double numberOne, double numberTwo) throws RemoteException {
        if (numberTwo < Double.parseDouble("0")) {
            return 0;
        }

        if (numberOne <= Double.parseDouble("0")) {
            return 0;
        }

        return Math.pow(numberTwo, 1/numberOne);
    }
}
