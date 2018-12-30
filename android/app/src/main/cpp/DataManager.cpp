//
// Created by Roberto on 26/12/2018.
//

#include <string>
#include <cstdlib>
#include <android/log.h>
#include "DataManager.h"

#define LOGD(x,...) __android_log_print(ANDROID_LOG_DEBUG , x, ##__VA_ARGS__)
//#define LOGD(x,...)
DataManager& DataManager::GetInstance(){
    static DataManager instance;
    return instance;
}
bool DataManager::OpenData(std::string fileName){
    if(_db!= nullptr){
        sqlite3_close(_db);
    }
    if(!sqlite3_open(fileName.c_str(),&_db)){
        //Create tables if dont exist
        char *errMsg = 0;
        const char* sql1 = "CREATE TABLE IF NOT EXISTS registros(" \
                            "email TEXT PRIMARY KEY NOT NULL,"\
                            "nombre TEXT NOT NULL,"\
                            "rfc TEXT NOT NULL,"\
                            "empresa TEXT NOT NULL,"\
                            "paswword TEXT NOT NULL);";
        if(sqlite3_exec(_db, sql1, ignore_callback, 0, &errMsg)!=SQLITE_OK){
            LOGD("GetUserInfo","sql1 %s",errMsg);
            sqlite3_free(errMsg);
            return false;
        }
        const char* sql2 = "CREATE TABLE IF NOT EXISTS sucursales(" \
                            "id INTEGER PRIMARY KEY AUTOINCREMENT,"\
                            "userEmail TEXT NOT NULL,"\
                            "sucursal TEXT NOT NULL,"\
                            "calle TEXT NOT NULL,"\
                            "colonia TEXT NOT NULL,"\
                            "numero INTEGER NOT NULL,"\
                            "cpostal INTEGER NOT NULL,"\
                            "ciudad TEXT NOT NULL,"\
                            "pais TEXT NOT NULL);";
        if(sqlite3_exec(_db, sql2, ignore_callback, 0, &errMsg)!=SQLITE_OK){
            LOGD("GetUserInfo","sql2 %s",errMsg);
            sqlite3_free(errMsg);
            return false;
        }
        const char* sql3 = "CREATE TABLE IF NOT EXISTS empleados(" \
                            "rfc TEXT PRIMARY KEY,"\
                            "sucursalID INTEGER NOT NULL,"\
                            "nombre TEXT NOT NULL,"\
                            "puesto TEXT NOT NULL);";
        if(sqlite3_exec(_db, sql3, ignore_callback, 0, &errMsg)!=SQLITE_OK){
            LOGD("GetUserInfo","sql3 %s",errMsg);
            sqlite3_free(errMsg);
            return false;
        }
        return true;
    }
    return false;
}
void DataManager::CloseData(){
    if(_db!= nullptr){
        sqlite3_close(_db);
    }
}
const char *DataManager::GetErrorMessage()
{
    if(_db!= nullptr) {
        return sqlite3_errmsg(_db);
    }else{
        return "none database is open";
    }
}

bool DataManager::CreateRegister(std::string FullName, std::string Email, std::string RFC, std::string EnterpriseName, std::string Password)
{
    char *errMsg = 0;
    std::string sql = "INSERT INTO registros(email,nombre,rfc,empresa,paswword)"\
                        "VALUES('"+Email+"','"+FullName+"','"+RFC+"','"+EnterpriseName+"','"+Password+"');";
    if(sqlite3_exec(_db, sql.c_str(), ignore_callback, 0, &errMsg)!=SQLITE_OK){
        sqlite3_free(errMsg);
        return false;
    }
    return true;
}
int DataManager::CreateBranch(std::string UserEmail, std::string BranchName, std::string Street, std::string Colony,std::string Number, std::string PostalCode, std::string City, std::string Country)
{
    char *errMsg = 0;
    std::string sql = "INSERT INTO sucursales(userEmail,sucursal,calle,colonia,numero,cpostal,ciudad,pais)"\
                        "VALUES('"+UserEmail+"','"+BranchName+"','"+Street+"','"+Colony+"',"+Number+","+PostalCode+",'"+City+"','"+Country+"');";
    if(sqlite3_exec(_db, sql.c_str(), ignore_callback, 0, &errMsg)!=SQLITE_OK){
        LOGD("CreateBranch","%s",sql.c_str());
        LOGD("CreateBranch","insert %s",errMsg);
        LOGD("CreateBranch","errmsg %s",sqlite3_errmsg(_db));
        sqlite3_free(errMsg);
        return -1;
    }
    sql = "SELECT last_insert_rowid();";
    if(sqlite3_exec(_db, sql.c_str(), insertedIndex_callback, 0, &errMsg)!=SQLITE_OK){
        LOGD("CreateBranch","get index %s",errMsg);
        sqlite3_free(errMsg);
        return -2;
    }
    return _inserted_index;
}
int DataManager::SaveBranch(std::string id, std::string BranchName, std::string Street, std::string Colony,std::string Number, std::string PostalCode, std::string City, std::string Country)
{
    char *errMsg = 0;
    std::string sql = "UPDATE sucursales SET "\
                        "sucursal='"+BranchName+"',calle='"+Street+"',colonia='"+Colony+"',numero="+Number+",cpostal="+PostalCode+",ciudad='"+City+"',pais='"+Country+"' WHERE id="+id+";";
    if(sqlite3_exec(_db, sql.c_str(), ignore_callback, 0, &errMsg)!=SQLITE_OK){
        LOGD("SaveBranch","%s",sql.c_str());
        LOGD("SaveBranch","insert %s",errMsg);
        LOGD("SaveBranch","errmsg %s",sqlite3_errmsg(_db));
        sqlite3_free(errMsg);
        return false;
    }
    return true;
}
bool DataManager::Login(std::string Email,std::string Password)
{
    char *errMsg = 0;
    _logedin = false;
    std::string sql = "SELECT paswword FROM registros WHERE email='"+Email+"';";
    LOGD("Login","%s",sql.c_str());
    if(sqlite3_exec(_db, sql.c_str(), login_callback, (void *)Password.c_str(), &errMsg)!=SQLITE_OK){
        LOGD("Login","%s",errMsg);
        sqlite3_free(errMsg);
    }
    LOGD("Login","_logedin %s",_logedin?"true":"false");
    return _logedin;
}
char *DataManager::GetUserInfo(std::string Email)
{
    char *errMsg = 0;
    _json="[";
    std::string sql = "SELECT nombre,rfc,empresa FROM registros WHERE email='"+Email+"';";
    LOGD("GetUserInfo","%s",sql.c_str());
    if(sqlite3_exec(_db, sql.c_str(), json_callback, 0, &errMsg)!=SQLITE_OK){
        LOGD("GetUserInfo","%s",errMsg);
        sqlite3_free(errMsg);
    }else{
        _json+="]";
    }
    LOGD("GetUserInfo","_json %s",_json.c_str());
    return const_cast<char *>(_json.c_str());
}
char *DataManager::GetBranchesByUser(std::string Email)
{
    char *errMsg = 0;
    _json="[";
    std::string sql = "SELECT id,sucursal FROM sucursales WHERE userEmail='"+Email+"';";
    LOGD("GetBranchesByUser","%s",sql.c_str());
    if(sqlite3_exec(_db, sql.c_str(), json_callback, 0, &errMsg)!=SQLITE_OK){
        LOGD("GetBranchesByUser","%s",errMsg);
        sqlite3_free(errMsg);
    }else{
        _json+="]";
    }
    LOGD("GetBranchesByUser","_json %s",_json.c_str());
    return const_cast<char *>(_json.c_str());
}
char *DataManager::GetBranchByID(std::string id)
{
    char *errMsg = 0;
    _json="[";
    std::string sql = "SELECT sucursal,calle,colonia,numero,cpostal,ciudad,pais FROM sucursales WHERE id="+id+";";
    LOGD("GetBranchByID","%s",sql.c_str());
    if(sqlite3_exec(_db, sql.c_str(), json_callback, 0, &errMsg)!=SQLITE_OK){
        LOGD("GetBranchByID","%s",errMsg);
        sqlite3_free(errMsg);
    }else{
        _json+="]";
    }
    LOGD("GetBranchesByUser","_json %s",_json.c_str());
    return const_cast<char *>(_json.c_str());
}
int DataManager::ignore_callback(void *NotUsed, int argc, char **argv, char **azColName)
{
    return 0;
}
int DataManager::login_callback(void *data, int argc, char **argv, char **azColName)
{
    LOGD("login_callback","columns: %d", argc);
    if(argc==1){
        LOGD("login_callback","%s == %s", argv[0],(char *)data);
    }
    if(argc==1 && std::string(argv[0])==std::string((char *)data)){
        DataManager::GetInstance()._logedin=true;
    }
    return 0;
}
int DataManager::insertedIndex_callback(void *NotUsed, int argc, char **argv, char **azColName)
{
    LOGD("insertedIndex_callback","columns: %d", argc);
    if(argc==1){
        LOGD("insertedIndex_callback","%s > 0", argv[0]);
    }
    int index=atoi(argv[0]);
    if(argc==1 && index>0){
        DataManager::GetInstance()._inserted_index=index;
    }else{
        DataManager::GetInstance()._inserted_index=-1;
    }
    return 0;
}
int DataManager::json_callback(void *NotUsed, int argc, char **argv, char **azColName)
{
    LOGD("json_callback","columns: %d", argc);
    if(DataManager::GetInstance()._json.length()>1) {
        DataManager::GetInstance()._json += ",{";
    } else {
        DataManager::GetInstance()._json += "{";
    }
    for(int i=0;i<argc;i++) {
        DataManager::GetInstance()._json+="\"";
        DataManager::GetInstance()._json+=azColName[i];
        DataManager::GetInstance()._json+="\":\"";
        DataManager::GetInstance()._json+=argv[i];
        DataManager::GetInstance()._json+="\"";
        if(i+1<argc){
            DataManager::GetInstance()._json+=",";
        }
    }
    DataManager::GetInstance()._json += "}";
    return 0;
}