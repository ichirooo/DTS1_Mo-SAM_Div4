package com.salamander.mo_sam_div4_dts1.proses.callback;

import com.salamander.mo_sam_div4_dts1.object.Customer;

import java.util.ArrayList;

public class CallbackCustomer {

    public interface CBFind {
        void onCB(ArrayList<Customer> customers);
    }

    public interface CBGenerateCode {
        void onCB(String customer_code);
    }

    public interface CBPosting {
        void onCB(Customer customer);
    }
}
