$(document).ready(function () {

  const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  $(document).ajaxSend(function (e, xhr, options) {
    xhr.setRequestHeader(header, token);
  });

  document.getElementById('amountPMB').addEventListener('input', function () {
    // Récupérer la valeur actuelle du champ de saisie
    let inputValue = this.value;

    // Limiter la saisie à deux chiffres après la virgule
    let decimalCount = (inputValue.split('.')[1] || []).length;
    if (decimalCount > 2) {
      // Tronquer la valeur pour ne conserver que deux chiffres après la virgule
      this.value = parseFloat(inputValue).toFixed(2);
    }
  });

  document.getElementById('decrementPMB').addEventListener('click', function () {
    var amountInput = document.getElementById('amountPMB');
    var currentValue = parseFloat(amountInput.value);
    if (currentValue > 0) {
      amountInput.value = Math.max((currentValue - 1), 0).toFixed(2);
    }
  });

  document.getElementById('incrementPMB').addEventListener('click', function () {
    var amountInput = document.getElementById('amountPMB');
    var currentValue = parseFloat(amountInput.value);
    amountInput.value = Math.min((currentValue + 1), 3000).toFixed(2);
  });

  document.getElementById('amountBank').addEventListener('input', function () {
    // Récupérer la valeur actuelle du champ de saisie
    let inputValue = this.value;

    // Limiter la saisie à deux chiffres après la virgule
    let decimalCount = (inputValue.split('.')[1] || []).length;
    if (decimalCount > 2) {
        // Tronquer la valeur pour ne conserver que deux chiffres après la virgule
        this.value = parseFloat(inputValue).toFixed(2);
    }
});

  document.getElementById('decrementBank').addEventListener('click', function () {
    var amountInput = document.getElementById('amountBank');
    var currentValue = parseFloat(amountInput.value);
    if (currentValue > 0) {
      amountInput.value = Math.max((currentValue - 1), 0).toFixed(2);
    }
  });

  document.getElementById('incrementBank').addEventListener('click', function () {
    var amountInput = document.getElementById('amountBank');
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
  document.getElementById('amountPMB').addEventListener('input', function () {
    updateValue(this, 3000);
  });

  // Gestionnaire d'événement pour le champ Bank
  document.getElementById('amountBank').addEventListener('input', function () {
    updateValue(this, 3000);
  });

  $("#sendPMB").click(function (event) {
    var amountPMB = $("#amountPMB").val();

    $.ajax({
      type: "POST",
      url: "/profileBank/creditAccount",
      data: { amountPMB: amountPMB },
      success: function (response) {
        if (response === "Account credited") {
          $("#profileBank-message").text("Success credited!").show().css('background-color', 'rgb(92, 184, 92)').css('color', 'white');
          setTimeout(function () {
            location.reload();
          }, 1000);
        } else {
          $("#profileBank-message").text("Our application is encountering an exception, please try again later.").show();
        }
      }
    });
  });

  $("#sendBank").click(function (event) {
    var amountBank = $("#amountBank").val();

    $.ajax({
      type: "POST",
      url: "/profileBank/debitAccount",
      data: { amountBank: amountBank },
      success: function (response) {
        if (response === "Account debited") {
          $("#profileBank-message").text("Success debited!").show().css('background-color', 'rgb(92, 184, 92)').css('color', 'white');
          setTimeout(function () {
            location.reload();
          }, 1000);
        } else if (response === "Balance insufficient") {
          $("#profileBank-message").text("Your Balance is insufficient.").show();
        } else {
          $("#profileBank-message").text("Our application is encountering an exception, please try again later.").show();
        }
      }
    });
  });


});



