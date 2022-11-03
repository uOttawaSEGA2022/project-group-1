package com.example.sdklab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void newProduct(View view) {
        TextView productIDText = findViewById(R.id.productIDValue);
        EditText productNameText = findViewById(R.id.productNameValue);
        EditText productSKUText = findViewById(R.id.productSKUValue);

        // Validate input fields
        boolean valid = true;

        String productname = productNameText.getText().toString().trim();
        if (productname.equals("")) {
            productNameText.setError("Product must have a name.");
            valid = false;
        }

        int sku = 0;
        try {
            sku = Integer.parseInt(productSKUText.getText().toString().trim());
        } catch (NumberFormatException e) {
            productSKUText.setError("SKU must be a positive integral number.");
            valid = false;
        }

        if (!valid)
            return;

        // Warn of duplicate if productID is already set
        if (!productIDText.getText().toString().equals("")) {
            Toast.makeText(this, "Making a duplicate of this product.", Toast.LENGTH_SHORT).show();
        }

        // Create and add the product
        Product product = new Product();
        product.setProductName(productname);
        product.setSku(sku);

        myDBHandler dbHandler = new myDBHandler(this);
        dbHandler.addProduct(product);

        Toast.makeText(this, "Product added!", Toast.LENGTH_SHORT).show();
    }

    public void findProduct(View view) {
        TextView productIDText = findViewById(R.id.productIDValue);
        EditText productNameText = findViewById(R.id.productNameValue);
        EditText productSKUText = findViewById(R.id.productSKUValue);

        // Validate input fields
        boolean valid = true;

        String productname = productNameText.getText().toString().trim();
       if (productname.equals("")) {
            productNameText.setError("Must specify a product name.");
            valid = false;
        }

        if (!valid)
            return;

        // Fetch product
        myDBHandler dbHandler = new myDBHandler(this);
        Product product = dbHandler.findProduct(productname);

       // Warn the user if the product doesn't exist and clear ID and SKU fields
        if (product == null) {
            Toast.makeText(this, "No product with this name exists.", Toast.LENGTH_SHORT).show();
            productIDText.setText("");
            productSKUText.setText("");
            return;
        }

        // Set ID and SKU fields with database data
        productIDText.setText(String.valueOf(product.getId()));
        productSKUText.setText(String.valueOf(product.getSku()));

        Toast.makeText(this, "Product found!", Toast.LENGTH_SHORT).show();
    }

    public void removeProduct(View view) {
        TextView productIDText = findViewById(R.id.productIDValue);
        EditText productNameText = findViewById(R.id.productNameValue);
        EditText productSKUText = findViewById(R.id.productSKUValue);

        // Validate input fields
        boolean valid = true;

       String productname = productNameText.getText().toString().trim();
      if (productname.equals("")) {
            productNameText.setError("Must specify a product name.");
            valid = false;
        }

        if (!valid)
            return;

        // Try deleting the product
        myDBHandler dbHandler = new myDBHandler(this);
        if (dbHandler.deleteProduct(productname)) {
            Toast.makeText(this, "Product deleted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No product to delete with this name exists.", Toast.LENGTH_SHORT).show();
        }

        // Reset ID and SKU in either case
        productIDText.setText(String.valueOf(""));
        productSKUText.setText(String.valueOf(""));

    }
}