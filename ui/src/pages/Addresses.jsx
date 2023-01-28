import React from 'react';
import useFetch from "react-fetch-hook";
import {Button, Container} from 'react-bootstrap'
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function succesToast(text){
  toast.success(text,{
    position: "bottom-center",
    autoClose: 100000,
    hideProgressBar: true,
    closeOnClick: true,
    pauseOnHover: false,
    draggable: true,
    progress: undefined,
    theme: "light",
    });  
}


async function deleteAddress(id){
  axios.delete(`/addresses/deleteAddress=${id}`,{
    method: "POST"
  });

  toast.error('Skasowano adres', {
    position: "bottom-center",
    autoClose: 100000,
    hideProgressBar: true,
    closeOnClick: true,
    pauseOnHover: false,
    draggable: true,
    progress: undefined,
    theme: "light",
    });

    await window.location.reload(false);
}

async function addAddressToDatabase(dataForm){
  axios.post(`/addresses/addAddress`, dataForm)
  .then(function (response) {
    succesToast("Pomyślnie dodano adres do bazy");
  })
  .catch(function (error) {
    console.log(error);
  });

  await window.location.reload(false);
}

function Addresses() {
    const { isLoading, data, error} = useFetch("/addresses");
    if (isLoading) return <div>Loading...</div>
    if (error) return<div>{`There is a problem fetching the post data - ${error}`}</div>

    const formData = new FormData();
    formData.append("city", "Zagan");
    formData.append("zipCode", "68-100");
    formData.append("street","Morelowa");
    formData.append("streetNumber","12");
    formData.append("apartmentNumber", "21");

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
                <Button variant='danger' size='sm' onClick={() => deleteAddress(address.id)}>Kasuj</Button>
              </td>
            </tr>)}
          )}
          </tbody>
        </table>

        <div>
          <table className='table table-bordered table-light, text-center'>
            <tbody>
              <tr>
                <td>Ulica</td>
                <td>Numer domu</td>
                <td>Numer mieszkania</td>
              </tr>
              
            </tbody>
          </table>
          <Button className='btn-success' size='sm' onClick={() => addAddressToDatabase(formData)}>Dodaj adres</Button>
          <ToastContainer />
        </div>
      </div>
    )
}

export default Addresses;