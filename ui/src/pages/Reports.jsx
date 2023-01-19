import React from 'react';
import useFetch from "react-fetch-hook";
import {Table, Button} from 'react-bootstrap'

function Reports() {
    const { isLoading, data, error} = useFetch("/reports");
    if (isLoading) return <div>Loading...</div>
    if (error) return<div>{`There is a problem fetching the post data - ${error}`}</div>
  
    return (
      <div>
        <table className="table table-light, text-center">
          <thead>
          <tr>
            <td>ID</td><td>Nazwa</td><td>Opis</td><td>Skasuj</td>
          </tr>
          </thead>
          <tbody>
          {data.map((report) =>
            {return (<tr key={report.id}>
              <td>{report.id}</td>
              <td>{report.name}</td>
              <td>{report.description}</td>
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

export default Reports;