import React from 'react';
import useFetch from "react-fetch-hook";
import {Table, Button} from 'react-bootstrap'

const API_URL = "/users";

function Users() {
    const { isLoading, data, error} = useFetch(API_URL);
    if (isLoading) return <div>Pobieranie danych...</div>
    if (error) return<div>{`Problem podczas odczytu danych - ${error}`}</div>
  
    return (
      <div>
        <table className="table table-bordered table-light, text-center">
          <thead>
          <tr>
            <td>ID</td><td>FIRST NAME</td><td>LAST NAME</td><td>Skasuj</td>
          </tr>
          </thead>
          <tbody>
          {data.map((user) =>
            {return (<tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.firstName}</td>
              <td>{user.lastName}</td>
              <td><Button variant="danger" size="sm">
                Delete
              </Button>{' '}
              </td>
            </tr>)}
          )}
          </tbody>
        </table>
      </div>
    )
}

export default Users;