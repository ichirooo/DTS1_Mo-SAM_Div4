package co.id.datascrip.mo_sam_div4_dts1.callback;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.object.Customer;

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
