import React, { useState } from 'react';
import useFetch from "react-fetch-hook";
import {Table, Button} from 'react-bootstrap'
import {deleteFromDatabase, addToDatabase} from './scripts/databaseOperations';

const API_URL = "/customers";

function Customers() {
    const { isLoading, data, error} = useFetch(API_URL);
    
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');

    if (isLoading) return <div>Pobieranie danych...</div>
    if (error) return<div>{`Problem podczas odczytu danych - ${error}`}</div>
  
    return (
      <div>
        <table className="table table-bordered table-light, text-center">
          <thead>
          <tr>
            <td>ID</td>
            <td>Imie</td>
            <td>Nazwisko</td>
            <td>Skasuj</td>
          </tr>
          </thead>
          <tbody>
          {data.map((customer) =>
            {return (<tr key={customer.id}>
              <td>{customer.id}</td>
              <td>{customer.firstName}</td>
              <td>{customer.lastName}</td>
              <td>
                <Button variant='danger' size='sm' onClick={() => deleteFromDatabase(API_URL,customer.id)}>Skasuj</Button>  
              </td>
            </tr>)}
          )}
          </tbody>
        </table>
      </div>
    )
}

export default Customers;