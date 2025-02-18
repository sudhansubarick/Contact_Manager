
const baseURL = "http://localhost:8080";
const viewContactModel = document.getElementById('view_contact_modal');
// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};

const contactModal = new Modal(viewContactModel,options,instanceOptions);

function openContactModel(){
    contactModal.show();
}
function closeContactModal(){
    contactModal.hide();
}

async function loadContactdata(id){

    //function to load data
  try {
    const data = await(await fetch(`${baseURL}/api/contacts/${id}`)).json();
    console.log(data);

    document.querySelector("#contact_name").innerHTML=data.name;
    document.querySelector("#contact_email").innerHTML=data.email;
    document.querySelector("#contact_phone").innerHTML=data.phoneNumber;
    document.querySelector("#contact_address").innerHTML=data.address;
    document.querySelector("#contact_description").innerHTML=data.description;
    document.querySelector("#contact_webLink").innerHTML=data.websiteLink;
    document.querySelector("#contact_linkedInLink").innerHTML=data.linkedInLink;
    
    document.querySelector("#contact_image").src=data.picture;

    const contactFavorite = document.querySelector("#contact_favorite");

    if(data.favorite){
      contactFavorite.innerHTML ="<i class='fa-solid fa-star'></i>";
    }else{
      contactFavorite.innerHTML="Not Favorite";
    }
    
    // Touch to Open the link
    document.querySelector("#contact_webLink").href = data.websiteLink;
    document.querySelector("#contact_webLink").innerHTML = data.websiteLink;
    document.querySelector("#contact_linkedInLink").href = data.linkedInLink;
    document.querySelector("#contact_linkedInLink").innerHTML = data.linkedInLink;
    
   

    
    openContactModel();
  } catch (error) {
    console.log("Error",error);
  }

}

//delete contact
async function deleteContact(id){
  const swalWithBootstrapButtons = Swal.mixin({
    customClass: {
      confirmButton: "btn btn-success",
      cancelButton: "btn btn-danger"
    },
    buttonsStyling: false
  });
  swalWithBootstrapButtons.fire({
    title: "Are you sure?",
    text: "You won't be able to revert this!",
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: "Yes, delete it!",
    cancelButtonText: "No, cancel!",
    reverseButtons: true
  }).then((result) => {
    if (result.isConfirmed) {
     const url = `${baseURL}/user/contacts/delete/`+id;
     window.location.replace(url);
    } 
    });
    } 
 