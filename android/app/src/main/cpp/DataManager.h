//
// Created by Roberto on 26/12/2018.
//

#ifndef ANDROID_DATAMANAGER_H
#define ANDROID_DATAMANAGER_H

#include "sqlite3.h"

class DataManager {
    sqlite3 *_db;
    bool _logedin;
    int _inserted_index;
    std::string _json;
    DataManager():_db(nullptr),_logedin(false){}
public:
    DataManager(DataManager const&) = delete;
    void operator=(DataManager const&) = delete;

    static DataManager& GetInstance();
    bool OpenData(std::string fileName);
    void CloseData();
    const char *GetErrorMessage();

    //Especifico del programa
    bool CreateRegister(std::string FullName, std::string Email, std::string RFC, std::string EnterpriseName, std::string Password);
    int CreateBranch(std::string UserEmail, std::string BranchName, std::string Street, std::string Colony,std::string Number, std::string PostalCode, std::string City, std::string Country);
    int SaveBranch(std::string id, std::string BranchName, std::string Street, std::string Colony,std::string Number, std::string PostalCode, std::string City, std::string Country);
    bool Login(std::string Email,std::string Password);
    char *GetUserInfo(std::string Email);
    char *GetBranchesByUser(std::string Email);
    char *GetBranchByID(std::string id);

    //Callbacks
    static int ignore_callback(void *NotUsed, int argc, char **argv, char **azColName);
    static int login_callback(void *data, int argc, char **argv, char **azColName);
    static int insertedIndex_callback(void *NotUsed, int argc, char **argv, char **azColName);
    static int json_callback(void *NotUsed, int argc, char **argv, char **azColName);
};


#endif //ANDROID_DATAMANAGER_H
