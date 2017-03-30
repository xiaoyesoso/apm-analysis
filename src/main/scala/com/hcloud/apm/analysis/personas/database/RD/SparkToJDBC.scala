package com.hcloud.apm.analysis.personas.database.RD

import java.sql.{Connection, PreparedStatement, ResultSet}

import com.hcloud.apm.analysis.personas.utils.DBConnectionPool

import scala.collection.Map
import scala.collection.mutable.ListBuffer


/**
  * Created by 陈志民 on 2016/9/14.
  *
  * 标签表格处理：需要插入数据Userid,label到标签表
  *
  */

object SparkToJDBC {

  def insertUserLabelData(iterator: Iterator[(Int, Int)]): Unit = {
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sql = "insert into user_label(userid, labelid) values (?, ?)"
    try {
       conn=DBConnectionPool.getJdbcConn()
       iterator.foreach(data => {
        ps = conn.prepareStatement(sql)

        ps.setInt(1, data._1)
        ps.setInt(2, data._2)

        ps.executeUpdate()
      }
      )
    } catch {
      case e: Exception => println("Mysql Exception"+e)
    } finally {
      if (ps != null) {
        ps.close()
      }
      if (conn != null) {
         DBConnectionPool.releaseConn(conn)
      }
    }
  }



  def isEmptyLabelData()={
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sql = "SELECT labelid FROM label WHERE modelid=1000"
    var empty =true
    try {
       conn=DBConnectionPool.getJdbcConn()
       ps = conn.prepareStatement(sql)


      val rst:ResultSet = ps.executeQuery()
      if(rst.next()){
        empty = false
      }
      if(empty==false){

        val sql2 = "DELETE FROM user_label WHERE labelid IN ( SELECT labelid FROM label WHERE modelid=1000)"
        ps = conn.prepareStatement(sql2)
        ps.executeUpdate()
        if(rst.next()){
          empty = false
        }

        val sql3 = "delete from label where modelid=1000"
        ps = conn.prepareStatement(sql3)
        ps.executeUpdate()

      }

     } catch {

      case e: Exception => println("Mysql Exception"+e)
     } finally {
       if (ps != null) {
        ps.close()
      }
      if (conn != null) {
         DBConnectionPool.releaseConn(conn)
      }
    }
  }

//  def isEmptyUserLabelData()={
//    var conn: Connection = null
//    var ps: PreparedStatement = null
//    val sql = "select user_label from label where modelid=1000)"
//    var empty =true
//    try {
//       conn=DBConnectionPool.getJdbcConn()
//       ps = conn.prepareStatement(sql)
//
//
//      val rst:ResultSet = ps.executeQuery()
//      if(rst.next()){
//        empty = false
//      }
//      if(empty==false){
//        val sql2 = "delete from label where modelid=1000"
//        ps = conn.prepareStatement(sql)
//        empty = ps.execute()
//      }
//
//    } catch {
//      case e: Exception => println("Mysql Exception"+e)
//     } finally {
//       if (ps != null) {
//        ps.close()
//      }
//      if (conn != null) {
//         DBConnectionPool.releaseConn(conn)
//      }
//    }
//  }

  def insertLabelData(iterator: Iterator[(Int, (String,Int))]): Unit = {
    isEmptyLabelData()
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sql = "insert into label(labelcenter, labelfeatures,useramounts,labelname,modelid) values (?, ?, ?,?,1000)"
    try {
       conn=DBConnectionPool.getJdbcConn()
       iterator.foreach(data => {
        ps = conn.prepareStatement(sql)

        ps.setString(1, data._1.toString)
        ps.setString(2, data._2._1)
        ps.setInt(3, data._2._2)
        ps.setString(4,"kmeans_label"+data._1.toString)
         ps.executeUpdate()
      })

    } catch {
      case e: Exception => println("Mysql Exception"+e)
    } finally {
       if (ps != null) {
        ps.close()
      }
      if (conn != null) {
         DBConnectionPool.releaseConn(conn)
      }
    }
  }

  def selectLabelId(label:Map[Int,String]): ListBuffer[Int] ={
    var conn: Connection = null
    var ps: PreparedStatement = null
    var data:String=""
    var index = 0
    label.keys.foreach(key=>{
      data += "\""+label(key)+"\""
      if (index<label.keys.size-1){
        data+=","
      }
      index+=1
    })
    val sql = "SELECT labelid from label where labelfeatures in ("+data+")"
     var labelids :ListBuffer[Int] = new ListBuffer[Int]
    try {
       conn=DBConnectionPool.getJdbcConn()
      ps = conn.prepareStatement(sql)
       val rst:ResultSet = ps.executeQuery()
      while(rst.next()){

        val labelid = rst.getInt(1)
        labelids+=labelid
      }
      labelids
    } catch {
      case e: Exception => println("mysql Exception"+e)
        ListBuffer(1)
    } finally {
      if (ps != null) {
        ps.close()
      }
      if (conn != null) {
        DBConnectionPool.releaseConn(conn)
      }
    }
  }

  def insertAllUsers(iterator: Iterator[(String,Int,Int,Int,Int)])= {
    isEmptyLabelData()
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sql = "insert into user(username,income,age,consumption,gender) values (?, ?, ?,?, ?)"
    try {
      conn = DBConnectionPool.getJdbcConn()
       ps = conn.prepareStatement(sql)
      iterator.foreach(data => {

        ps.setString(1, data._1)
        ps.setInt(2, data._2)
        ps.setInt(3,data._3)
        ps.setInt(4, data._4)
        ps.setInt(5, data._5)
        ps.executeUpdate()
       })

    } catch {
      case e: Exception => println("Mysql Exception"+e)
     } finally {
       if (ps != null) {
        ps.close()
      }
      if (conn != null) {
        DBConnectionPool.releaseConn(conn)
      }
    }
  }

  def insertUserAndLabel(iterator: Iterator[(Int,Int)])= {
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sql = "insert into user_label(userid,labelid) values(?,?)"
    try {

      conn = DBConnectionPool.getJdbcConn()
       iterator.foreach(data => {
        ps = conn.prepareStatement(sql)

        ps.setInt(1, data._1)
        ps.setInt(2, data._2)
         ps.executeUpdate()
      })
     } catch {
      case e: Exception => println("Mysql Exception"+e)
     } finally {
       if (ps != null) {
        ps.close()
      }
      if (conn != null) {

        DBConnectionPool.releaseConn(conn)
      }
    }
  }


  def selectLabelidByLabelcenter(labelcenter: Int):Int={
     var conn: Connection = null
    var ps: PreparedStatement = null
    val sql = "SELECT labelid from label where labelcenter= "+labelcenter+" and modelid = 1000"

    try {

        conn = DBConnectionPool.getJdbcConn()

        ps = conn.prepareStatement(sql)

        val rst:ResultSet = ps.executeQuery()
        var labelid = -1
        while(rst.next()){
          labelid = rst.getInt(1)
        }
        labelid

    } catch {
      case e: Exception => println("Mysql Exception"+e)
        -1

    } finally {
      if (ps != null) {
        ps.close()
      }
      if (conn != null) {
        //        conn.close()
        DBConnectionPool.releaseConn(conn)
      }
    }
  }

  def selectUserIDByName(username: String):Int={
    //    SELECT labelid from label where labelfeatures="(收入高,5):34:80079.33333333333"
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sql = "SELECT userid FROM user WHERE username = \""+username+"\""

    try {

      conn = DBConnectionPool.getJdbcConn()

      ps = conn.prepareStatement(sql)

      val rst:ResultSet = ps.executeQuery()
      var userid = -1
      while(rst.next()){
        userid = rst.getInt(1)
      }
      userid

    } catch {
      case e: Exception => println("Mysql Exception"+e)
        -1

    } finally {
      if (ps != null) {
        ps.close()
      }
      if (conn != null) {
        //        conn.close()
        DBConnectionPool.releaseConn(conn)
      }
    }
  }

}

