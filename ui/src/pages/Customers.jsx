import React from 'react';
import useFetch from "react-fetch-hook";
import {Table, Button} from 'react-bootstrap'

function Customers() {
    const { isLoading, data, error} = useFetch("/customers");
    if (isLoading) return <div>Loading...</div>
    if (error) return<div>{`There is a problem fetching the post data - ${error}`}</div>
  
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
              <td><Button variant="danger" size="sm">
                Delete
              </Button>{''}
              </td>
            </tr>)}
          )}
          </tbody>
        </table>
      </div>
    )
}

export default Customers;