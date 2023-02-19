import React from 'react';
import useFetch from "react-fetch-hook";
import {Table, Button} from 'react-bootstrap'
import {deleteFromDatabase, addToDatabase} from './scripts/databaseOperations';
import { useState } from 'react';

const API_URL = "/reports"

function Reports() {
    const { isLoading, data, error} = useFetch(API_URL);
    if (isLoading) return <div>Pobieranie danych...</div>
    if (error) return<div>{`Problem podczas odczytu danych - ${error}`}</div>
  
    return (
      <div>
        <table className="table table-bordered table-light, text-center">
          <thead>
          <tr>
            <td>ID</td><td>Nazwa</td><td>Opis</td><td>Data dodania</td><td>Data ukończenia</td><td>Data aktualizacji</td><td></td>
          </tr>
          </thead>
          <tbody>
          {data.map((report) =>
            {return (<tr key={report.id}>
              <td>{report.id}</td>
              <td>{report.name}</td>
              <td>{report.description}</td>
              <td>{report.createDate}</td>
              <td>{report.endDate}</td>
              <td>{report.updateDate}</td>
              <td>
                <div class="d-flex justify-content-center">  
                  <tr>
                    <td>
                      <Button size="sm">
                        Edytuj
                      </Button>{''}
                    </td>
                    <td>
                    <Button variant='danger' size='sm' onClick={() => deleteFromDatabase(API_URL, report.id)}>Skasuj</Button>
                    </td>
                  </tr>
                </div>
              </td>
            </tr>)}
          )}
          </tbody>
        </table>
      </div>
    )
}

export default Reports;