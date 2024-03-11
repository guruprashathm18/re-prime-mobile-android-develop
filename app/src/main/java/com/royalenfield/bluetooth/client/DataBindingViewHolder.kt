package com.royalenfield.bluetooth.client

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Venkat on 6/27/2022.
 */
abstract class DataBindingViewHolder<T: ViewDataBinding>(var binding: T) :RecyclerView.ViewHolder(binding.root)