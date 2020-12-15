package com.company.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SpecialCalculator extends Remote {

    double perCent(double numberOne, double numberTwo) throws RemoteException;
    double elevation(double numberOne, double numberTwo) throws RemoteException;
    double root(double numberOne, double numberTwo) throws RemoteException;

}