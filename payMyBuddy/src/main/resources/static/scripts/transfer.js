// Variables to manage pagination :
var rowsPerPage = 3;
var totalPages = 0;
var totalRows = 0;
var rows = 0;
var currentPage = 1;

$(document).ready(function () {
  // Event listeners and input validation for the 'amount' input field :
  document.getElementById('amount').addEventListener('input', function () {
    let inputValue = this.value;
    let decimalCount = (inputValue.split('.')[1] || []).length;
    if (decimalCount > 2) {
      this.value = parseFloat(inputValue).toFixed(2);
    }
  });

  // Event listeners for decrementing  the 'amount' input field :
  document.getElementById('decrement').addEventListener('click', function () {
    var amountInput = document.getElementById('amount');
    var currentValue = parseFloat(amountInput.value);
    if (currentValue > 0) {
      amountInput.value = (currentValue - 1).toFixed(2);
    }
  });

  // Event listeners for  incrementing the 'amount' input field :
  document.getElementById('increment').addEventListener('click', function () {
    var amountInput = document.getElementById('amount');
    var currentValue = parseFloat(amountInput.value);
    amountInput.value = Math.min((currentValue + 1), 3000).toFixed(2);
  });

  // Common function to update input values with a maximum limit of 3000 :
  function updateValue(inputElement, maxValue) {
    var currentValue = parseFloat(inputElement.value);
    if (isNaN(currentValue) || currentValue > maxValue) {
      inputElement.value = maxValue.toFixed(2);
    }
  }


  // Event listener using the common function for 'amount' input field :
  document.getElementById('amount').addEventListener('input', function () {
    updateValue(this, 3000);
  });

  // CSRF token setup for AJAX requests :
  const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  $(document).ajaxSend(function (e, xhr, options) {
    xhr.setRequestHeader(header, token);
  });

  // Clear modal inputs and messages on modal close :
  $('#modal-add-connection').on('hidden.bs.modal', function (e) {
    $('#modal-email-add-connection').val('');
    $('#text-modal-add-connection').hide();
  });

  // AJAX request to add a connection :
  $("#submit-button-add-connection").click(function (event) {
    var mail = $("#modal-email-add-connection").val();
    $.ajax({
      type: "POST",
      url: "/transfer/addConnection",
      data: { mail: mail },
      success: function (response) {
        if (response === "mail-Added") {
          $("#text-modal-add-connection").text("Success added email!").show().css('background-color', 'rgb(92, 184, 92)').css('color', 'white');
          setTimeout(function () {
            location.reload();
          }, 1000);
        } else {
          $("#text-modal-add-connection").text("An unknown error occurred.").show();
        }
      },
      error: function (response) {
        var responseText = response.responseText;
        console.log(responseText);
        if (responseText === "mail-empty") {
          $("#text-modal-add-connection").text("You need to entry an email.").show();
        } else if (responseText === "mail-nonexistent") {
          $("#text-modal-add-connection").text("This email does not exist.").show();
        } else if (responseText === "mail-FromCurrentUser") {
          $("#text-modal-add-connection").text("This email belongs to the current user.").show();
        } else if (responseText === "mail-FromContactList") {
          $("#text-modal-add-connection").text("This email is already in your contacts.").show();
        } else {
          $("#text-modal-add-connection").text("An unknown error occurred.").show();
        }
      }
    });
  });

  // Open payment modal and perform input validations :
  $('#open-modal-pay').click(function () {
    var amountValue = parseFloat(document.getElementById('amount').value);
    var connectionValue = document.querySelector('select.form-select').value;
    var showAlert = false;
    if (amountValue < 1 || !Number.isInteger(amountValue)) {
      alert("Le montant doit être supérieur à 1 euro et être un nombre entier.");
      showAlert = true;
      return;
    }
    if (connectionValue === 'Select a connection') {
      alert("Veuillez sélectionner une connexion valide.");
      showAlert = true;
      return;
    }
    if (!showAlert) {
      var connectionSelect = document.querySelector('select.form-select');
      var modalInput = document.getElementById('modal-email-pay');
      var amountSelect = document.getElementById('amount');
      var amountInputModal = document.getElementById('modal-amount');
      var descriptionReset = document.getElementById('modal-description');
      descriptionReset.value = null;
      var today = new Date();
      var date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
      var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
      var dateTime = date + ' ' + time;
      var dateInputModal = document.getElementById('modal-date');
      var selectedConnection = connectionSelect.value;
      var selectedAmount = amountSelect.value;
      modalInput.value = selectedConnection;
      amountInputModal.value = selectedAmount;
      dateInputModal.value = dateTime;
      $('#modal-pay').modal('show');
    }
  });

  // Clear modal inputs and messages on modal close :
  $('#modal-pay').on('hidden.bs.modal', function (e) {
    $('#modal-description').val('');
    $('#text-modal-pay').hide();
  });

  // AJAX request to make a payment :
  $("#submit-button-pay").click(function (event) {
    var amountValue = document.getElementById('modal-amount').value;
    var connectionValue = document.getElementById('modal-email-pay').value;
    var descriptionValue = document.getElementById('modal-description').value;
    var dateValue = document.getElementById('modal-date').value;
    $.ajax({
      type: "POST",
      url: "/transfer/pay",
      data: {
        amount: amountValue,
        connection: connectionValue,
        description: descriptionValue,
        date: dateValue
      },
      success: function (response) {
        if (response ==="payDone") {
          $("#text-modal-pay").text("Success payment!").show().css('background-color', 'rgb(92, 184, 92)').css('color', 'white');
          setTimeout(function () {
            location.reload();
          }, 1000);
        } else {
          $("#text-modal-pay").text("An exception occured, please retry later.").show();
        }
      },
      error: function (response) {
        var responseText = response.responseText;
        if (responseText === "errorException") {
          $("#text-modal-pay").text("An exception occured, please retry later.").show();
        } else if (responseText === "emptyDescription") {
          $("#text-modal-pay").text("You need to entry a description.").show();
        } else if (responseText === "bankBalanceInsufficient") {
          $("#text-modal-pay").text("You sold is insufficient.").show();
        }
      }
    });
  });

  // Pagination setup :
  totalRows = $('#customTable tbody tr').length;
  console.log('totalRows:', totalRows);
  totalPages = Math.ceil(totalRows / rowsPerPage);
  rows = document.getElementById('customTable').querySelectorAll('tbody tr');
  dataRows = document.getElementById('customTable').querySelectorAll('tbody tr');

  // Add alternating row colors and display the first page :
  for (var i = 0; i < rows.length; i++) {
    if (i % 2 === 0) {
      rows[i].classList.add('bg-white');
    } else {
      rows[i].classList.add('bg-grey');
    }
  }
  showPage(1);
  currentPage = 1;
  generatePaginationLinks(totalPages);
});

// Function to display rows for a specific page :
function showPage(pageNumber) {
  if (pageNumber === 1) {
    var startIndex = 0;
    var endIndex = startIndex + rowsPerPage;
  } else {
    var startIndex = (pageNumber - 1) * rowsPerPage;
    var endIndex = startIndex + rowsPerPage - 1;
  }
  console.log('startIndex:', startIndex);
  console.log('endIndex:', endIndex);
  for (var i = 1; i < rows.length; i++) {
    rows[i].style.display = 'none';
  }
  for (var j = startIndex; j <= endIndex && j < rows.length; j++) {
    rows[j].style.display = 'table-row';
  }
}

// Function to generate pagination links :
function generatePaginationLinks(totalPages) {
  var paginationList = $('#pagination');
  paginationList.empty();
  paginationList.append('<li class="page-item"><a class="page-link" href="#" onclick="prevPage()">&laquo;</a></li>');
  for (var i = 1; i <= totalPages; i++) {
    paginationList.append('<li class="page-item"><a class="page-link" href="#" onclick="showPage(' + i + ')">' + i + '</a></li>');
  }
  paginationList.append('<li class="page-item"><a class="page-link" href="#" onclick="nextPage()">&raquo;</a></li>');
}

// Function to navigate to the previous page :
function prevPage() {
  if (currentPage > 1) {
    showPage(parseInt(currentPage) - 1);
    currentPage = currentPage - 1;
  }
}

// Function to navigate to the next page :
function nextPage() {
  console.log('currentPage:', currentPage);
  console.log('totalPages:', totalPages);
  if (currentPage < totalPages) {
    showPage(parseInt(currentPage) + 1);
    currentPage = currentPage + 1;
  }
}