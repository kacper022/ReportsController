import React from 'react';
import useFetch from "react-fetch-hook";
import {Table, Button} from 'react-bootstrap'

function Users() {
    const { isLoading, data, error} = useFetch("/users");
    if (isLoading) return <div>Loading...</div>
    if (error) return<div>{`There is a problem fetching the post data - ${error}`}</div>
  
    return (
      <div>
        <table className="table table-light, text-center">
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