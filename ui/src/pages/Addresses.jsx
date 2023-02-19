import React from 'react';
import useFetch from "react-fetch-hook";
import {Button, Form} from 'react-bootstrap'
import {deleteFromDatabase, addToDatabase} from './scripts/databaseOperations';
import { useState } from 'react';

const API_URL = "/addresses"

function Addresses() {
    const { isLoading, data, error} = useFetch(API_URL);
    const [city, setCity] = useState(' ');
    const [zipCode, setZipCode] = useState(' ');
    const [street, setStreet] = useState(' ');
    const [streetNumber, setStreetNumber] = useState(' ');
    const [apartmentNumber, setApartmentNumber] = useState(' ');

    if (isLoading) return <div>Pobieranie danych...</div>
    if (error) return<div>{`Problem podczas odczytu danych - ${error}`}</div>

    const dataToSend = new FormData();
    
    const submitButtonClick = () => {
      dataToSend.append("city", city);
      dataToSend.append("street", street);
      dataToSend.append("zipCode", zipCode);
      dataToSend.append("streetNumber", streetNumber);
      dataToSend.append("apartmentNumber", apartmentNumber);

      addToDatabase(API_URL, dataToSend);
    }

    return (
      <div>
        <table className="table table-bordered table-light, text-center">
          <thead>
          <tr>
            <td>ID</td>
            <td>Miasto</td>
            <td>Kod pocztowy</td>
            <td>Ulica</td>
            <td>Nr. domu</td>
            <td>Nr. mieszkania</td>
          </tr>
          </thead>
          <tbody>
          {data.map((address) =>
            {return (<tr key={address.id}>
              <td>{address.id}</td>
              <td>{address.city}</td>
              <td>{address.zipCode}</td>
              <td>{address.street}</td>
              <td>{address.streetNumber}</td>
              <td>{address.apartmentNumber}</td>
              <td><Button variant="primary" size="sm">
                Edytuj
              </Button>
              </td>
              <td>
                <Button variant='danger' size='sm' onClick={() => deleteFromDatabase(API_URL,address.id)}>Kasuj</Button>
              </td>
            </tr>)}
          )}
          </tbody>
        </table>

        <Form onSubmit={submitButtonClick}>
          <div className="form-group">
            <label htmlFor='city'>Miasto</label>
            <input id='city' name='city' type='text' className="form-control" placeholder='Miasto' onChange={event => setCity(event.target.value)}></input>
          </div>

          <div className="form-group">
            <label htmlFor='zipCode'>Kod pocztowy</label>
            <input id='zipCode' name='zipCode' type='text' className="form-control" placeholder='Kod pocztowy' onChange={event => setZipCode(event.target.value)}></input>
          </div>

          <div className="form-group">
            <label htmlFor='street'>Ulica</label>
            <input id='street' name='street' type='text' className="form-control" placeholder='Ulica/Aleja' onChange={event => setStreet(event.target.value)}></input>
          </div>

          <div className="form-group">
            <label htmlFor='streetNumber'>Nr. budynku</label>
            <input id='streetNumber' name='streetNumber' type='text' className="form-control" placeholder='Numer budynku' onChange={event => setStreetNumber(event.target.value)}></input>
          </div>

          <div className="form-group">
            <label htmlFor='apartmentNumber'>Nr. mieszkania</label>
            <input id='apartmentNumber' name='apartmentNumber' type='text' className="form-control" placeholder='Numer mieszkania' onChange={event => setApartmentNumber(event.target.value)}></input>
          </div>

          <Button type="submit" className='btn-sm'>Dodaj adres</Button>
        </Form>
      </div>
    )
}

export default Addresses;