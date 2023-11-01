$(document).ready(function () {
  var rows = document.getElementById('customTable').querySelectorAll('tbody tr');
  for (var i = 0; i < rows.length; i++) {
    if (i % 2 === 0) {
      rows[i].classList.add('bg-white');
    } else {
      rows[i].classList.add('bg-grey');
    }
  }

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
    amountInput.value = (currentValue + 1).toFixed(2);
  });

  const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');

  $(document).ready(function () {

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
          console.log("good");
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
          console.error("feffefe");
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

    $("#submit-button-pay").click(function (event) {
      var amountValue = document.getElementById('modal-amount').value;
      var connectionValue = document.getElementById('modal-email-pay').value;
      var descriptionValue = document.getElementById('modal-description').value;
      var dateValue = document.getElementById('modal-date').value;
      console.log(amountValue);
      console.log(connectionValue);
      console.log(descriptionValue);
      console.log(dateValue);


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
          if (response === "payDone") {

          }
        }
      });
    });
  });
});