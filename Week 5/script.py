import pandas as pd
import pyodbc
import sqlite3
from settings import Settings, logger

def process():
    DB = {'servername': 'LAPTOP-2HKQUAKD\SQLEXPRESS', 'database': 'Datawarehouse4.3 (1)'}

    export_conn = pyodbc.connect('DRIVER={SQL Server};SERVER=LAPTOP-2HKQUAKD\\SQLEXPRESS' +
                             ';DATABASE=Datawarehouse4.3 (1)' + ';Trusted_connection=yes')

    export_cursor = export_conn.cursor()

    go_sales = sqlite3.connect('go_sales.sqlite')
    go_crm = sqlite3.connect('go_crm.sqlite')
    go_staff = sqlite3.connect('go_staff.sqlite')

    product = pd.DataFrame(pd.read_sql_query("SELECT * FROM product", go_sales))

    for index, row in product.iterrows():
        try:
            query = f"""
        INSERT INTO PRODUCT(PRODUCT_product_number, PRODUCT_product_image, PRODUCT_product_name, PRODUCT_description, PRODUCT_product_type_code, PRODUCT_INTRODUCTION_DATE_date, PRODUCT_LANGUAGE_name, PRODUCT_PRODUCTION_COST_number, PRODUCT_MARGIN_number)
        VALUES ('{row['PRODUCT_NUMBER']}', '{row['PRODUCT_IMAGE']}', '{row['PRODUCT_NAME']}', '{row['DESCRIPTION']}', '{row['PRODUCT_TYPE_CODE']}', '{row['INTRODUCTION_DATE']}', '{row['LANGUAGE']}', '{row['PRODUCTION_COST']}', '{row['MARGIN']}')
        """
            export_cursor.execute(query)
        except pyodbc.Error:
            print(query)

    export_conn.commit()

    RETAILER_SITE = pd.DataFrame(pd.read_sql_query("SELECT * FROM retailer_site", go_sales))

    for index, row in RETAILER_SITE.iterrows():
        try:
            query = f"""
            INSERT INTO RETAILER_SITE(RETAILER_SITE_retailer_site_code, RETAILER_SITE_RETAILER_CODE_number, RETAILER_SITE_ADRESS1_adress, RETAILER_SITE_ADRESS2_adress, RETAILER_SITE_CITY_name, RETAILER_SITE_REGION_name, RETAILER_SITE_postal_zone, RETAILER_SITE_COUNTRY_CODE_number, RETAILER_SITE_ACTIVE_INDICATOR_number) 
            VALUES ('{row['RETAILER_SITE_CODE']}', '{row['RETAILER_CODE']}', '{row['ADDRESS1']}', '{row['ADDRESS2']}', '{row['CITY']}', '{row['REGION']}', '{row['POSTAL_ZONE']}', '{row['COUNTRY_CODE']}', '{row['ACTIVE_INDICATOR']}')
         """
            export_cursor.execute(query)
        except pyodbc.Error:
            print(query)
        
    export_conn.commit()

    order_method = pd.DataFrame(pd.read_sql_query("SELECT * FROM order_method", go_sales))

    for index, row in order_method.iterrows():
        try:
            query = f"""
            INSERT INTO ORDER_METHOD(ORDER_METHOD_order_met, ORDER_METHOD_ORDER_METHOD_EN_name)
            VALUES ('{row['ORDER_METHOD_CODE']}', '{row['ORDER_METHOD_EN']}')
            """
            export_cursor.execute(query)
        except pyodbc.Error:
            print(query)
        
    export_conn.commit()

    ORDER_DETAILS = pd.DataFrame(pd.read_sql_query("SELECT * FROM order_details", go_sales))

    for index, row in ORDER_DETAILS.iterrows():
        try:
            query = f"""
            UPDATE ORDER_HEADER 
            SET ORDER_HEADER_PRODUCT_product_number = {row['PRODUCT_NUMBER']}
            """
            export_cursor.execute(query)
        except pyodbc.Error:
            print(query)
        
    export_conn.commit()

    ORDER_HEADER = pd.DataFrame(pd.read_sql_query("SELECT * FROM order_header", go_sales))

    for index, row in ORDER_HEADER.iterrows():
        try:
            query = f"""
            INSERT INTO ORDER_HEADER(ORDER_HEADER_order_number, ORDER_HEADER_retailer_name, ORDER_HEADER_retailer_contact_code, ORDER_HEADER_ORDER_DATE_nr, ORDER_HEADER_RETAILER_SITE_retailer_site_code, ORDER_HEADER_SALES_STAFF_sales_staff_code, ORDER_HEADER_ORDER_METHOD_order_method)
            VALUES ('{row['ORDER_NUMBER']}', '{row['RETAILER_NAME']}', '{row['RETAILER_CONTACT_CODE']}', '{row['ORDER_DATE']}', '{row['RETAILER_SITE_CODE']}', '{row['SALES_STAFF_CODE']}', '{row['ORDER_METHODE_CODE']}')
            """
            export_cursor.execute(query)
        except pyodbc.Error:
            print(query)

    export_conn.commit()

    SATISFACTION = pd.DataFrame(pd.read_sql_query("SELECT * FROM satisfaction", go_staff))

    for index, row in SATISFACTION.iterrows():
        try:
            query = f"""
            INSERT INTO ORDER_METHOD(SATISFACTION_YEAR_nr, SATISFACTION_TYPE_satisfaction_type_code, SATISFACTION_SALES_STAFF_sales_staff_code)
            VALUES ('{row['YEAR']}', '{row['SATISFACTION_TYPE_CODE']}', '{row['SALES_STAFF_CODE']}')
            """
            export_cursor.execute(query)
        except pyodbc.Error:
            print(query)
        
    export_conn.commit()

    SALES_STAFF = pd.DataFrame(pd.read_sql_query("SELECT * FROM satisfaction", go_staff))

    for index, row in SALES_STAFF.iterrows():
        try:
            query = f"""
            INSERT INTO ORDER_METHOD(SALES_STAFF_sales_staff_code, SALES_STAFF_first_name, SALES_STAFF_last_name, SALES_STAFF_fax, SALES_STAFF_work_phone, SALES_STAFF_email, SALES_STAFF_DATE_HIRED_date, SALES_STAFF_BRANCH_CODE_number, SALES_STAFF_MANAGER_CODE_id)
            VALUES ('{row['SALES_STAFF_CODE']}', '{row['FIRST_NAME']}', '{row['LAST_NAME']}', '{row['FAX']}', '{row['WORK_PHONE']}', '{row['EMAIL']}', '{row['DATE_HIRED']}', '{row['SALES_BRANCH_CODE']}', '{row['MANAGER_CODE']}')
            """
            export_cursor.execute(query)
        except pyodbc.Error:
            print(query)
        
    export_conn.commit()

    export_cursor.close()
