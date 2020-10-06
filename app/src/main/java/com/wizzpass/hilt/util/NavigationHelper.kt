package com.wizzpass.hilt.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.wizzpass.hilt.db.entity.Resident

/**
 * Created by novuyo on 20,September,2020
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransactionWithHistory { replace(frameId, fragment) }
}
fun AppCompatActivity.replaceFragmentWithNoHistory(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransactionWithoutHistory { replace(frameId, fragment) }
}

inline fun FragmentManager.inTransactionWithHistory(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().addToBackStack("Added Fragment").commit()
}
inline fun FragmentManager.inTransactionWithoutHistory(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}
fun FragmentActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransactionWithHistory { replace(frameId, fragment) }
}

fun FragmentActivity.replaceFragmentWithNoHistory(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransactionWithoutHistory { replace(frameId, fragment) }
}


inline fun FragmentManager.inTransactionWithoutHistoryData(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun FragmentActivity.replaceFragmentWithDataTest(fragment: Fragment, frameId: Int, resident : Resident) {
    supportFragmentManager.beginTransaction().replace(frameId, fragment).commit()
    supportFragmentManager.inTransactionWithoutHistoryData {
        val bundle = Bundle()
        bundle.putParcelable("resident", resident)
        val transaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        transaction.replace(frameId, fragment)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        //transaction.commit()
    }
}

fun FragmentActivity.replaceFragmentWithResidentAndSearchField(fragment: Fragment, frameId: Int, resident : Resident, searchString: String) {
    supportFragmentManager.beginTransaction().replace(frameId, fragment).commit()
    supportFragmentManager.inTransactionWithoutHistoryData {
        val bundle = Bundle()
        bundle.putParcelable("resident", resident)
        bundle.putString("searchField", searchString)
        val transaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        transaction.replace(frameId, fragment)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        //transaction.commit()
    }
}

fun FragmentActivity.replaceFragmentWithResidentAndSearchFieldVisitor(fragment: Fragment, frameId: Int, resident : Resident,inputText :String, searchString: String) {
    supportFragmentManager.beginTransaction().replace(frameId, fragment).commit()
    supportFragmentManager.inTransactionWithoutHistoryData {
        val bundle = Bundle()
        bundle.putParcelable("resident", resident)
        bundle.putString("inputText", inputText)
        bundle.putString("searchField", searchString)
        val transaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        transaction.replace(frameId, fragment)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        //transaction.commit()
    }
}


fun FragmentActivity.replaceFragmentWithListDataTest(fragment: Fragment, frameId: Int, resident : ArrayList<Resident>) {
    supportFragmentManager.beginTransaction().replace(frameId, fragment).commit()
    supportFragmentManager.inTransactionWithoutHistoryData {
        val bundle = Bundle()
        bundle.putParcelableArrayList("resident", resident)
        val transaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        //transaction.replace(frameId, fragment)
        //transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        //transaction.commit()
    }
}

fun FragmentActivity.replaceFragmentWithListDataAndSearchField(fragment: Fragment, frameId: Int, resident : ArrayList<Resident>,searchString :String) {
    supportFragmentManager.beginTransaction().replace(frameId, fragment).commit()
    supportFragmentManager.inTransactionWithoutHistoryData {
        val bundle = Bundle()
        bundle.putParcelableArrayList("resident", resident)
        bundle.putString("searchField", searchString)
        val transaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        //transaction.replace(frameId, fragment)
        //transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        //transaction.commit()
    }
}

fun FragmentActivity.replaceFragmentWithListDataAndSearchFieldVisitor(fragment: Fragment, frameId: Int, resident : ArrayList<Resident>,searchString :String, isVisitor : Boolean) {
    supportFragmentManager.beginTransaction().replace(frameId, fragment).commit()
    supportFragmentManager.inTransactionWithoutHistoryData {
        val bundle = Bundle()
        bundle.putParcelableArrayList("resident", resident)
        bundle.putString("searchField", searchString)
        bundle.putBoolean("isVisitor", isVisitor)
        val transaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        //transaction.replace(frameId, fragment)
        //transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        //transaction.commit()
    }
}


fun FragmentActivity.replaceFragmentWithStringData(fragment: Fragment, frameId: Int, inputext : String, searchString :String) {
    supportFragmentManager.beginTransaction().replace(frameId, fragment).commit()
    supportFragmentManager.inTransactionWithoutHistoryData {
        val bundle = Bundle()
        bundle.putString("inputText", inputext)
        bundle.putString("searchField", searchString)
        val transaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

    }
}

fun FragmentActivity.replaceFragmentWithStringDataForVisitor(fragment: Fragment, frameId: Int, inputext : String, searchString :String, isVisitor : Boolean) {
    supportFragmentManager.beginTransaction().replace(frameId, fragment).commit()
    supportFragmentManager.inTransactionWithoutHistoryData {
        val bundle = Bundle()
        bundle.putString("inputText", inputext)
        bundle.putString("searchField", searchString)
        bundle.putBoolean("isVisitor", isVisitor)
        val transaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

    }
}

fun FragmentActivity.replaceFragmentWithStringData(fragment: Fragment, frameId: Int, carReg : String, mobile :String, address : String, resident: Resident) {
    supportFragmentManager.beginTransaction().replace(frameId, fragment).commit()
    supportFragmentManager.inTransactionWithHistory {
        val bundle = Bundle()
        bundle.putString("carReg", carReg)
        bundle.putString("mobile", mobile)
        bundle.putString("address", address)
        bundle.putParcelable("resident",resident)
        val transaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

    }
}

fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    var intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}