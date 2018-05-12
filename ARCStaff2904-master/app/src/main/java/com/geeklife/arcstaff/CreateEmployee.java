package com.geeklife.arcstaff;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geeklife.common.Business;
import com.geeklife.common.Country;
import com.geeklife.common.Office;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateEmployee extends AppCompatActivity {

    static final String BUSINESS = "Business Unit";
    static final String OFFICE = "Home Office";
    static final String COUNTRY = "Country Code";
    static final String NEW_USER = "Create New User";

    EditText fName, lName, bUnit, hOffice, emailAdd, countryCode, contact;
    Office office;
    TextView newUser,
            tv_fName, err_fName,
            tv_lName, err_lName,
            tv_bUnit, err_bUnit,
            tv_hOffice, err_hOffice,
            tv_emailAdd, err_emailAdd,
            tv_countryCode, err_countryCode,
            tv_contact, err_contact;

    ListView buList, offList, countryList;

    // define array of business names from Business enum
    String buItems[] = {
            Business.DEFENCE.getLongName(),
            Business.NUCLEAR.getLongName(),
            Business.OILGAS.getLongName(),
            Business.RAIL.getLongName(),
            Business.OPERATIONS.getLongName(),
            Business.RESOURCE.getLongName()
    };

    String countryItems[] = {
            Country.UK.getLongName(),
            Country.AUSTRALIA.getLongName(),
            Country.SINGAPORE.getLongName()
    };

    // define array of office names from Office enum
    String officeItems[] = {
            Office.BRISTOL.getLongName(),
            Office.EDINBURGH.getLongName(),
            Office.GLASGOW.getLongName(),
            Office.LONDON.getLongName(),
            Office.MANCHESTER.getLongName(),
            Office.TAUNTON.getLongName(),
            Office.SINGAPORE.getLongName(),
            Office.PERTH.getLongName(),
            Office.SYDNEY.getLongName()
    };


    JSONObject employee;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.new_user );

        final InputMethodManager imm = ( InputMethodManager ) getSystemService( INPUT_METHOD_SERVICE );

        // ArrayAdapters required
        final ArrayAdapter busAdapter = new ArrayAdapter<>
                ( this, R.layout.lv_rows, buItems );
        final ArrayAdapter offAdapter = new ArrayAdapter<>
                ( this, R.layout.lv_rows, officeItems );
        final ArrayAdapter countryAdapter = new ArrayAdapter<>
                ( this, R.layout.lv_rows, countryItems );

        final Button enter = findViewById( R.id.btn_enter );

        newUser = findViewById( R.id.new_user );

        // for first name
        fName = findViewById( R.id.first_name );
        tv_fName = findViewById( R.id.tv_first_name );
        err_fName = findViewById( R.id.err_first_name );

        // for last name
        lName = findViewById( R.id.last_name );
        tv_lName = findViewById( R.id.tv_last_name );
        err_lName = findViewById( R.id.err_last_name );

        // for business unit
        bUnit = findViewById( R.id.bus_unit );
        tv_bUnit = findViewById( R.id.tv_bus_unit );
        err_bUnit = findViewById( R.id.err_bus_unit );

        // for home Office
        hOffice = findViewById( R.id.home_office );
        tv_hOffice = findViewById( R.id.tv_home_office );
        err_hOffice = findViewById( R.id.err_home_office );

        // for email address
        emailAdd = findViewById( R.id.email );
        tv_emailAdd = findViewById( R.id.tv_email );
        err_emailAdd = findViewById( R.id.err_email );

        // for country code
        countryCode = findViewById( R.id.country_code );
        tv_countryCode = findViewById( R.id.tv_country_code );
        err_countryCode = findViewById( R.id.err_country_code );

        // for contact number
        contact = findViewById( R.id.contact );
        tv_contact = findViewById( R.id.tv_contact );
        err_contact = findViewById( R.id.err_contact );

        employee = new JSONObject();

        View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange( View v, boolean hasFocus ) {

                switch ( v.getId() ) {

                    // Business Unit
                    case R.id.bus_unit:
                        if ( hasFocus ) {
                            buList = findViewById( R.id.list_view );

                            buList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick
                                        ( AdapterView<?> adapterView, View view, int pos, long len ) {
                                    String buName = buItems[pos];
                                    bUnit.setText( new StringBuilder()
                                            .append( buName )
                                            .append( " " )
                                            .append( getString( R.string.open_parenthesis ) )
                                            .append( Business.getBusFromName( buName ).getBuCode() )
                                            .append( getString( R.string.close_parenthesis ) )
                                            .toString() );
                                    newUser.setText( NEW_USER );
                                    toggleVisibility();
                                }
                            } );

                            Log.i( "FORM", "BU has focus" );
                            imm.hideSoftInputFromWindow( bUnit.getWindowToken(), 0 );
                            buList.setAdapter( busAdapter );
                            newUser.setText( "Select " + BUSINESS );
                            toggleVisibility();
                        }
                        break;

                    case R.id.home_office:
                        if ( hasFocus ) {
                            offList = findViewById( R.id.list_view );

                            offList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick
                                        ( AdapterView<?> adapterView, View view, int pos, long len ) {
                                    String officeName = officeItems[pos];
                                    office = Office.getOfficeFromName( officeName );
                                    setCountryCode( office );
                                    hOffice.setText( new StringBuilder()
                                            .append( officeName )
                                            .append( " " )
                                            .append( getString( R.string.open_parenthesis ) )
                                            .append( Office.getOfficeFromName( officeName ).getCode() )
                                            .append( getString( R.string.close_parenthesis ) )
                                            .toString() );
                                    newUser.setText( NEW_USER );
                                    toggleVisibility();
                                }
                            } );

                            Log.i( "FORM", "OFFICE has focus" );
                            imm.hideSoftInputFromWindow( hOffice.getWindowToken(), 0 );
                            offList.setAdapter( offAdapter );
                            newUser.setText( "Select " + OFFICE );
                            toggleVisibility();
                        }
                        break;

                    case R.id.country_code:
                        if ( hasFocus ) {
                            // TODO : check consistency of office and country
                            countryList = findViewById( R.id.list_view );

                            countryList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick
                                        ( AdapterView<?> adapterView, View view, int pos, long len ) {
                                    String country = countryItems[pos];
                                    countryCode.setText( new StringBuilder()
                                            .append( country )
                                            .append( " " )
                                            .append( "(" )
                                            .append( Country.getCountryFromName( country ).getIntCode() )
                                            .append( ")" )
                                            .toString() );
                                    newUser.setText( NEW_USER );
                                    toggleVisibility();
                                }
                            } );

                            Log.i( "FORM", "COUNTRY has focus" );
                            imm.hideSoftInputFromWindow( countryCode.getWindowToken(), 0 );
                            countryList.setAdapter( countryAdapter );
                            newUser.setText( "Select " + COUNTRY );
                            toggleVisibility();
                        }
                        break;
                }
            }
        };


        enter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {

                imm.hideSoftInputFromWindow( enter.getWindowToken(), 0 );

                // build JSON Object

                try {
                    employee.put( "first_name", fName.getText().toString() );
                    employee.put( "last_name", lName.getText().toString() );
                    employee.put( "bus_unit", bUnit.getText().toString() );
                    employee.put( "home_office", hOffice.getText().toString() );
                    employee.put( "email", emailAdd.getText().toString() );
                    employee.put( "contact", contact.getText().toString() );

                } catch ( JSONException e ) {
                    e.printStackTrace();
                }

                if ( checkInputText() ) {
                    new AsyncCreateEmp().execute();
                }

            }
        } );

        bUnit.setOnFocusChangeListener( focusListener );
        hOffice.setOnFocusChangeListener( focusListener );
        countryCode.setOnFocusChangeListener( focusListener );

    }

    private class AsyncCreateEmp extends AsyncTask<Void, Void, Void> {
        HttpURLConnection connection;
        URL url = null;
        ProgressBar progressBar = findViewById( R.id.progressbar );

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //set a process dialogue on UI thread
            progressBar.setVisibility( View.VISIBLE );

        }

        @Override
        protected Void doInBackground( Void... voids ) {
            return null;
        }

        @Override
        protected void onPostExecute( Void v ) {
            super.onPostExecute( v );
            progressBar.setVisibility( View.GONE );
            Toast.makeText( CreateEmployee.this, "DONE", Toast.LENGTH_SHORT ).show();
        }
    }

    private ArrayAdapter setArrayAdapter( String arrayToUse[] ) {

        ArrayAdapter mAdapter = new ArrayAdapter<>
                ( this, R.layout.lv_rows, arrayToUse );
        buList.setAdapter( mAdapter );

        return mAdapter;
    }

    private boolean checkInputText() {
        Boolean isValid = true;
        EditText editTexts[] = {fName, lName, bUnit, hOffice, emailAdd, countryCode, contact};
        TextView titles[] = {tv_fName, tv_lName, tv_bUnit, tv_hOffice, tv_emailAdd, tv_countryCode, tv_contact};
        TextView errors[] = {err_fName, err_lName, err_bUnit, err_hOffice, err_emailAdd, err_countryCode, err_contact};

        for ( int i = 0; i < editTexts.length; i++ ) {
            EditText et = editTexts[i];
            TextView title = titles[i];
            TextView error = errors[i];

            // loop through the editTexts to check input
            if ( et == fName || et == lName ) {
                String REGEX = "[a-zA-Z-]";
                String input = et.getText().toString();
                Pattern p = Pattern.compile( REGEX );
                Matcher m = p.matcher( input );
                int compare = findMatches( m, input ) - input.length();
                int hyphen = input.indexOf( "-" );
                Boolean validHyphen = ( hyphen == -1 || hyphen < input.length() - 2 );


                if ( compare == 0 && input.length() > 0 && validHyphen ) {
                    error.setText( null );
                    et.setBackgroundResource( R.drawable.et_valid );
                } else {
                    if ( input.length() == 0 ) {
                        error.setText( R.string.error_required_field );
                    } else {
                        error.setText( R.string.error_invalid_name );
                    }
                    et.setBackgroundResource( R.drawable.et_error );
                    if ( isValid ) {
                        isValid = false;
                    }
                }
            }
            // just check not null
            if ( et == bUnit || et == hOffice || et == countryCode ) {
                if ( et.getText().length() > 0 ) {
                    error.setText( null );
                    et.setBackgroundResource( R.drawable.et_valid );
                } else {
                    error.setText( R.string.error_required_field );
                    et.setBackgroundResource( R.drawable.et_error );
                }
            }

            if ( et == contact ) {
                String REGEX = "[0-9]";
                String input = et.getText().toString();
                Pattern p = Pattern.compile( REGEX );
                Matcher m = p.matcher( input );
                int compare = findMatches( m, input ) - input.length();

                if ( compare == 0 && input.length() > 0 ) {
                    error.setText( null );
                    et.setBackgroundResource( R.drawable.et_valid );
                } else {
                    if ( input.length() == 0 ) {
                        error.setText( R.string.error_required_field );
                    }
                    et.setBackgroundResource( R.drawable.et_error );
                    if ( isValid ) {
                        isValid = false;
                    }
                }
            }

            if ( et == emailAdd ) {
                String arc = "@consultarc.com";
                String emailToTest = et.getText().toString();

                if ( isValidEmail( emailToTest ) ) {
                    error.setText( null );
                    et.setBackgroundResource( R.drawable.et_valid );
                } else {
                    Log.i( "EMAILTEST", "NOT OK" );
                    if ( emailToTest.length() == 0 ) {
                        error.setText( R.string.error_required_field );
                    } else {
                        error.setText( "ERROR: Invalid email" );
                    }
                    et.setBackgroundResource( R.drawable.et_error );
                    if ( isValid ) {
                        isValid = false;

                    }
                }
            }
        }


        return isValid;
    }

    private boolean isValidEmail( String email ) {
        Boolean ok = true;

        String arc = "@consultarc.com";
        String REGEX = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        ok = email.length() > 15;

        if ( ok ) {
            ok = email.substring( email.length() - arc.length() ).equals( arc );
        }

        Pattern p = Pattern.compile( REGEX );
        Matcher m = p.matcher( email );

        if ( ok ) {
            ok = m.matches();
        }

        // dummy
        return ok;  // use ok to add multiple tests

    }

    private int findMatches( Matcher m, String s ) {
        int count = 0;

        while ( m.find() ) {
            count++;
        }
        return count;
    }

    private void toggleVisibility() {
        ConstraintLayout form = findViewById( R.id.new_user_form );
        Log.i( "FORM", Integer.toString( form.getChildCount() ) );

        for ( int i = 0; i < form.getChildCount(); i++ ) {
            View v = form.getChildAt( i );

            if ( v.getId() != R.id.new_user ) {

                if ( v.getVisibility() == View.GONE ) {
                    v.setVisibility( View.VISIBLE );
                } else {
                    v.setVisibility( View.GONE );
                }
            }
        }

    }

    private void setCountryCode( Office office ) {

        // set country name of office
        String country = null;
        switch ( office ) {
            case BRISTOL:
            case EDINBURGH:
            case GLASGOW:
            case LONDON:
            case MANCHESTER:
            case TAUNTON:
                country = Country.UK.getLongName();
                break;
            case PERTH:
            case SYDNEY:
                country = Country.AUSTRALIA.getLongName();
                break;
            case SINGAPORE:
                country = Country.SINGAPORE.getLongName();
                break;
        }

        countryCode.setText( new StringBuilder()
                .append( country )
                .append( " " )
                .append( "(" )
                .append( Country.getCountryFromName( country ).getIntCode() )
                .append( ")" )
                .toString() );
    }


    public String getBuCode( String name ) {
        switch ( name ) {
            case "Defence":
                return Business.DEFENCE.getBuCode();
            case "Nuclear":
                return Business.NUCLEAR.getBuCode();
            case "Oil & Gas":
                return Business.OILGAS.getBuCode();
            case "Rail":
                return Business.RAIL.getBuCode();
            case "Operations":
                return Business.OPERATIONS.getBuCode();
            case "Resource Management":
                return Business.RESOURCE.getBuCode();
        }
        return null;
    }
}
