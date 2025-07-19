document.addEventListener('DOMContentLoaded', function() {
    console.log("DOMContentLoaded fired from main.js.");

    // Leer valores de las meta-etiquetas
    const rowIdField = document.querySelector('meta[name="row-id-field"]').getAttribute('content');
    const baseApiPath = document.querySelector('meta[name="base-api-path"]').getAttribute('content');
    const baseApiUrl = '/' + baseApiPath;
    
    // Opcional: para debug o lógica futura
    // const pageTitle = document.querySelector('meta[name="page-title"]').getAttribute('content');

    console.log("Base API URL for delete operations:", baseApiUrl);

    const selectAllCheckbox = document.getElementById("selectAllInTable");
    const deleteSelectedButton = document.getElementById("deleteSelectedBtn");
    const dynamicDeleteForm = document.getElementById("dynamicDeleteForm");
    const rowCheckboxes = dynamicDeleteForm
        ? dynamicDeleteForm.querySelectorAll('input[name="selectedIds"]')
        : [];

    const singleDeleteButtons = document.querySelectorAll(
        ".delete-single-button"
    );

    // Obtener CSRF Token de las meta-etiquetas
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    async function handleDelete(idsToDelete) {
        if (idsToDelete.length === 0) {
            alert("Por favor, selecciona al menos un elemento para eliminar.");
            return;
        }

        if (
            !confirm(
                "¿Estás seguro de que quieres eliminar los elementos seleccionados? Esta acción es irreversible."
            )
        ) {
            return;
        }

        try {
            let url = "";
            let method = "DELETE";

            let body = null;
            if (idsToDelete.length === 1) {
                url = `${baseApiUrl}/${idsToDelete[0]}`;
            } else {
                url = `${baseApiUrl}/batch`;
                body = JSON.stringify(idsToDelete);
            }

            let headers = {
                "Content-Type": "application/json",
            };

            if (csrfToken && csrfHeader) {
                headers[csrfHeader] = csrfToken;
                console.log(`CSRF Token added to headers: ${csrfHeader}=${csrfToken}`);
            } else {
                console.warn("CSRF Token or Header Name not found in meta tags. Requests might fail.");
            }

            const response = await fetch(url, {
                method: method,
                headers: headers,
                body: body,
            });

            if (response.ok || response.status === 204) {
                alert("Elementos eliminados exitosamente.");
                window.location.reload();
            } else {
                let errorData = {};
                try {
                    errorData = await response.json();
                } catch (e) {
                    errorData.message = response.statusText;
                }

                let errorMessage = `Error al eliminar: ${
                    errorData.message || "Error desconocido"
                }.`;
                if (response.status === 404) {
                    errorMessage =
                        "Error: El/los recurso(s) a eliminar no fue/fueron encontrado(s).";
                } else if (response.status === 400) {
                    errorMessage =
                        "Error: La solicitud es inválida (por ejemplo, IDs vacíos).";
                } else if (response.status === 403) {
                    errorMessage = "Acceso denegado. No tienes permisos para realizar esta acción. Asegúrate de estar logueado como un administrador.";
                }
                alert(errorMessage);
                console.error(
                    "Error en la eliminación (Estado HTTP:",
                    response.status,
                    "): ",
                    errorData
                );
            }
        } catch (error) {
            console.error(
                "Error de red o al comunicarse con el servidor:",
                error
            );
            alert(
                "Ocurrió un error de conexión. Por favor, verifica tu red e inténtalo de nuevo."
            );
        }
    }

    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener("change", function () {
            rowCheckboxes.forEach(
                (checkbox) => (checkbox.checked = this.checked)
            );
        });
    }

    if (deleteSelectedButton) {
        deleteSelectedButton.addEventListener("click", function (event) {
            event.preventDefault();
            const selectedIds = Array.from(rowCheckboxes)
                .filter((checkbox) => checkbox.checked)
                .map((checkbox) => parseInt(checkbox.value));
            handleDelete(selectedIds);
        });
    }

    if (singleDeleteButtons) {
        singleDeleteButtons.forEach((button) => {
            button.addEventListener("click", function (event) {
                event.preventDefault();
                const idToDelete = parseInt(this.dataset.id);
                handleDelete([idToDelete]);
            });
        });
    }
});



