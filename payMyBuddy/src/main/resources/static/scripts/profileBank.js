$(document).ready(function () {

  // Retrieve CSRF token from cookies and set it as a header for AJAX requests :
  const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  $(document).ajaxSend(function (e, xhr, options) {
    xhr.setRequestHeader(header, token);
  });


  // Limit decimal places to 2 for the input field with id 'amountPMB' :
  document.getElementById('amountPMB').addEventListener('input', function () {
    let inputValue = this.value;
    let decimalCount = (inputValue.split('.')[1] || []).length;
    if (decimalCount > 2) {
      this.value = parseFloat(inputValue).toFixed(2);
    }
  });

  // Decrease the value in 'amountPMB' input field by 1 (minimum value 0) :
  document.getElementById('decrementPMB').addEventListener('click', function () {
    var amountInput = document.getElementById('amountPMB');
    var currentValue = parseFloat(amountInput.value);
    if (currentValue > 0) {
      amountInput.value = Math.max((currentValue - 1), 0).toFixed(2);
    }
  });

  // Increase the value in 'amountPMB' input field by 1 (maximum value 3000) :
  document.getElementById('incrementPMB').addEventListener('click', function () {
    var amountInput = document.getElementById('amountPMB');
    var currentValue = parseFloat(amountInput.value);
    amountInput.value = Math.min((currentValue + 1), 3000).toFixed(2);
  });

  // Limit decimal places to 2 for the input field with id 'amountBank' :
  document.getElementById('amountBank').addEventListener('input', function () {
    let inputValue = this.value;
    let decimalCount = (inputValue.split('.')[1] || []).length;
    if (decimalCount > 2) {
      this.value = parseFloat(inputValue).toFixed(2);
    }
  });

  // Decrease the value in 'amountBank' input field by 1 (minimum value 0) :
  document.getElementById('decrementBank').addEventListener('click', function () {
    var amountInput = document.getElementById('amountBank');
    var currentValue = parseFloat(amountInput.value);
    if (currentValue > 0) {
      amountInput.value = Math.max((currentValue - 1), 0).toFixed(2);
    }
  });

  // Increase the value in 'amountPMB' input field by 1 (maximum value 3000) :
  document.getElementById('incrementBank').addEventListener('click', function () {
    var amountInput = document.getElementById('amountBank');
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

  // Common function to update input values with a maximum limit of 3000 :
  document.getElementById('amountPMB').addEventListener('input', function () {
    updateValue(this, 3000);
  });

  document.getElementById('amountBank').addEventListener('input', function () {
    updateValue(this, 3000);
  });

  // AJAX request to credit the PMB account :
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

  // AJAX request to debit the Bank account :
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
        } else if (response === "bankBalanceInsufficient") {
          $("#profileBank-message").text("Your Balance is insufficient.").show();
        } else {
          $("#profileBank-message").text("Our application is encountering an exception, please try again later.").show();
        }
      }
    });
  });


});