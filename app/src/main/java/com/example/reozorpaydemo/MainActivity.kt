package com.example.reozorpaydemo

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.razorpay.Checkout
import kotlinx.android.synthetic.main.activity_payment.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        /*
           * To ensure faster loading of the Checkout form,
           * call this method as early as possible in your checkout flow
           * */
        Checkout.preload(applicationContext)

        btn_pay.setOnClickListener {
            if (!TextUtils.isEmpty(edt_bill.text.toString())){
                startPayment(edt_bill.text.toString())
                layout_number .isErrorEnabled = false

            }
            else{
                layout_number .isErrorEnabled = true
                layout_number.error =getString(R.string.error_amount_empty)
               // Toast.makeText(this, getString(R.string.error_amount_empty), Toast.LENGTH_LONG).show()

            }
        }
        edt_bill.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                layout_number.error = null
            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
            override fun afterTextChanged(arg0: Editable) {}
        })
    }

    private fun startPayment(amount: String) {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name", "Razorpay Demo")
            options.put("description", "Demoing Charges")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
/*
            options.put("order_id", "order_DBJOWzybf0sJbb");
*/
            var total:Double= amount.toDouble()
            total *= 100
            options.put("amount", total)//pass amount in currency subunits

            val prefill = JSONObject()
            prefill.put("email", "gaurav.kumar@example.com")
            prefill.put("contact", "9876543210")

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public fun onPaymentSuccess(razorpayPaymentID: String) {
        try {
            Toast.makeText(this, "Payment Successful: $razorpayPaymentID", Toast.LENGTH_SHORT)
                .show();
        } catch (e: Exception) {
            Log.e("TAG", "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    fun onPaymentError(code: Int, response: String) {
        try {
            Toast.makeText(this, "Payment failed: $code $response", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            Log.e("TAG", "Exception in onPaymentError", e)
        }
    }
}