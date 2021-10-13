package com.dhruv.tipsy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.dhruv.tipsy.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.seekBarTip.progress = INITIAL_TIP_PERCENT
        binding.tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT %"
        updateTipDescription(INITIAL_TIP_PERCENT)
        binding.seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG,"onProgressChanged $p1")
                binding.tvTipPercentLabel.text = "$p1%"
                computeTipAndTotal()
                updateTipDescription(p1)


            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        binding.etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG,"afterTextChanged $p0")
                computeTipAndTotal()
            }

        })
        binding.etSplitAmongNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "after text changed in split among number")
                computeTipAndTotal()
            }

        })



    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when(tipPercent){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        binding.tvTipDescription.text = tipDescription

        // update the color according to tip percent
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat()/binding.seekBarTip.max,
            ContextCompat.getColor(this,R.color.color_worst_tip),
            ContextCompat.getColor(this,R.color.color_best_tip)
        )as Int
        binding.tvTipDescription.setTextColor(color)


    }

    private fun computeTipAndTotal() {
        if(binding.etBaseAmount.text.isEmpty() || binding.etSplitAmongNumber.text.isEmpty()){
            binding.tvTipAmount.text = ""
            binding.tvTotalAmount.text=""
            binding.tvBillPerPersonAmount.text=""
            return
        }
        // get the base amount and tip percent
        val baseAmount = binding.etBaseAmount.text.toString().toDouble()
        val tipPercent = binding.seekBarTip.progress
        val person = binding.etSplitAmongNumber.text.toString().toDouble()
        // calculate the tip and total
        val tipAmount = tipPercent*baseAmount/100
        val totalAmount = tipAmount+baseAmount
        val perPersonAmount = totalAmount/person
        // update the ui
        binding.tvTipAmount.text = "%.2f".format(tipAmount)
        binding.tvTotalAmount.text = "%.2f".format(totalAmount)
        binding.tvBillPerPersonAmount.text = "%.2f".format(perPersonAmount)


    }


}