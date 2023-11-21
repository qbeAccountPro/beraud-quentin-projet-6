var rowsPerPage = 3;
var totalPages = 0;
var totalRows = 0;
var rows = 0;
var currentPage = 1;



$(document).ready(function () {

  document.getElementById('amount').addEventListener('input', function () {
    // Récupérer la valeur actuelle du champ de saisie
    let inputValue = this.value;

    // Limiter la saisie à deux chiffres après la virgule
    let decimalCount = (inputValue.split('.')[1] || []).length;
    if (decimalCount > 2) {
      // Tronquer la valeur pour ne conserver que deux chiffres après la virgule
      this.value = parseFloat(inputValue).toFixed(2);
    }
  });

  document.getElementById('decrement').addEventListener('click', function () {
    var amountInput = document.getElementById('amount');
    var currentValue = parseFloat(amountInput.value);
    if (currentValue > 0) {
      amountInput.value = (currentValue - 1).toFixed(2);
    }
  });

  document.getElementById('increment').addEventListener('click', function () {
    var amountInput = document.getElementById('amount');
    var currentValue = parseFloat(amountInput.value);
    amountInput.value = Math.min((currentValue + 1), 3000).toFixed(2);
  });

    // Fonction pour mettre à jour la valeur du champ en fonction des limites
    function updateValue(inputElement, maxValue) {
      var currentValue = parseFloat(inputElement.value);
      if (isNaN(currentValue) || currentValue > maxValue) {
        inputElement.value = maxValue.toFixed(2);
      }
    }
  
    // Gestionnaire d'événement pour le champ PMB
    document.getElementById('amount').addEventListener('input', function () {
      updateValue(this, 3000);
    });

  const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  $(document).ajaxSend(function (e, xhr, options) {
    xhr.setRequestHeader(header, token);
  });

  $('#modal-add-connection').on('hidden.bs.modal', function (e) {
    $('#modal-email-add-connection').val(''); // Réinitialise l'e-mail
    $('#text-modal-add-connection').hide(); // Cache le message d'erreur
  });

  $("#submit-button-add-connection").click(function (event) {
    var mail = $("#modal-email-add-connection").val();

    $.ajax({
      type: "POST",
      url: "/transfer/addConnection",
      data: { mail: mail },
      success: function (response) {
        if (response === "mail-empty") {
          $("#text-modal-add-connection").text("You need to entry an email.").show();
        } else if (response === "mail-nonexistent") {
          $("#text-modal-add-connection").text("This email does not exist.").show();
        } else if (response === "mail-FromCurrentUser") {
          $("#text-modal-add-connection").text("This email belongs to the current user.").show();
        } else if (response === "mail-FromContactList") {
          $("#text-modal-add-connection").text("This email is already in your contacts.").show();
        } else if (response === "mail-Added") {
          $("#text-modal-add-connection").text("Success added email!").show().css('background-color', 'rgb(92, 184, 92)').css('color', 'white');
          setTimeout(function () {
            location.reload();
          }, 1000);
        } else {
          $("#text-modal-add-connection").text("An unknown error occurred.").show();
        }
      }
    });
  });

  $('#open-modal-pay').click(function () {
    var amountValue = parseFloat(document.getElementById('amount').value);
    var connectionValue = document.querySelector('select.form-select').value;
    var showAlert = false; // Variable pour suivre l'état des alertes

    // Vérification que le montant est supérieur à 1 euro et est un nombre entier
    if (amountValue < 1 || !Number.isInteger(amountValue)) {
      alert("Le montant doit être supérieur à 1 euro et être un nombre entier.");
      showAlert = true; // Définir la variable showAlert sur true en cas d'alerte
      return;
    }

    // Vérification que la connexion est différente de "Select a connection"
    if (connectionValue === 'Select a connection') {
      alert("Veuillez sélectionner une connexion valide.");
      showAlert = true; // Définir la variable showAlert sur true en cas d'alerte
      return;
    }

    if (!showAlert) {
      // Reste du code pour remplir les champs et ouvrir le modal
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

  $('#modal-pay').on('hidden.bs.modal', function (e) {
    $('#modal-description').val('');
    $('#text-modal-pay').hide();
  });

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
        if (response === "errorException") {
          $("#text-modal-pay").text("An exception occured, please retry later.").show();
        } else if (response === "emptyDescription") {
          $("#text-modal-pay").text("You need to entry a description.").show();
        } else if (response === "bankBalanceInsufficient") {
          $("#text-modal-pay").text("You sold is insufficient.").show();
        } else if (response === "payDone") {
          $("#text-modal-pay").text("Success payment!").show().css('background-color', 'rgb(92, 184, 92)').css('color', 'white');
          setTimeout(function () {
            location.reload();
          }, 1000);
        }
      }
    });
  });

  totalRows = $('#customTable tbody tr').length;
  console.log('totalRows:', totalRows);
  totalPages = Math.ceil(totalRows / rowsPerPage);
  rows = document.getElementById('customTable').querySelectorAll('tbody tr');
  dataRows = document.getElementById('customTable').querySelectorAll('tbody tr');


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

function showPage(pageNumber) {
  if (pageNumber === 1) {
    var startIndex = 0;
    var endIndex = startIndex + rowsPerPage;
  } else {
    var startIndex = (pageNumber - 1) * rowsPerPage;
    var endIndex = startIndex + rowsPerPage -1;
  }

  console.log('startIndex:', startIndex);
  console.log('endIndex:', endIndex);

  // Hide all data rows
  for (var i = 1; i < rows.length; i++) {
    rows[i].style.display = 'none';
  }

  // Display the data rows for the current page
  for (var j = startIndex; j <= endIndex && j < rows.length; j++) {
    rows[j].style.display = 'table-row';
  }
}

function generatePaginationLinks(totalPages) {
  var paginationList = $('#pagination');
  paginationList.empty(); // Clear existing pagination links

  // Previous button
  paginationList.append('<li class="page-item"><a class="page-link" href="#" onclick="prevPage()">&laquo;</a></li>');

  // Page numbers
  for (var i = 1; i <= totalPages; i++) {
    paginationList.append('<li class="page-item"><a class="page-link" href="#" onclick="showPage(' + i + ')">' + i + '</a></li>');
  }

  // Next button
  paginationList.append('<li class="page-item"><a class="page-link" href="#" onclick="nextPage()">&raquo;</a></li>');
}

function prevPage() {
  if (currentPage > 1) {
    showPage(parseInt(currentPage) - 1);
    currentPage = currentPage - 1;
  }
}

function nextPage() {
  console.log('currentPage:', currentPage);
  console.log('totalPages:', totalPages);


  if (currentPage < totalPages) {
    showPage(parseInt(currentPage) + 1);
    currentPage = currentPage + 1;
  }
}