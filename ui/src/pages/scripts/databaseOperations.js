import axios from "axios";

export const deleteFromDatabase = (url, id) => {
  axios.delete(`${url}/delete=${id}`,{
    method: "POST"
  });
    window.location.reload(false);
}

export const addToDatabase = (url, dataForm) => {
  axios.post(`${url}/add`, dataForm)
  .then(function (response) {
    console.log("Pomyślnie dodano do bazy");
    window.location.reload(false);
  })
  .catch(function (error) {
    console.log(error);
  });
}