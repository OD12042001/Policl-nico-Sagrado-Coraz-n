document.addEventListener('DOMContentLoaded', () => {
    const sidebar = document.querySelector('.sidebar');
    const hamburgerMenu = document.querySelector('.hamburger-menu');
    const overlay = document.querySelector('.overlay');
    const tableContentArea = document.querySelector('.table-content-area');

    // Función para abrir el sidebar
    function openSidebar() {
        sidebar.classList.add('active');
        overlay.classList.add('active');
        // Opcional: ajustar el margen del contenido para que el sidebar no lo tape
        // Si el sidebar es fixed y el contenido no tiene margen, el contenido se mantendrá estático.
        // Si quieres que el contenido se desplace un poco al abrir el sidebar, podrías añadir una clase aquí.
        // Por ahora, con position: fixed para el sidebar, el margen del content-area solo afecta al inicio.
    }

    // Función para cerrar el sidebar
    function closeSidebar() {
        sidebar.classList.remove('active');
        overlay.classList.remove('active');
    }

    // Evento para el botón de hamburguesa
    if (hamburgerMenu) {
        hamburgerMenu.addEventListener('click', () => {
            if (sidebar.classList.contains('active')) {
                closeSidebar();
            } else {
                openSidebar();
            }
        });
    }

    // Evento para el overlay (cerrar sidebar al hacer click fuera)
    if (overlay) {
        overlay.addEventListener('click', () => {
            closeSidebar();
        });
    }

    // Cierra el sidebar si el usuario hace clic en un enlace del sidebar (navegación interna)
    // Esto es útil en móviles para que al seleccionar una opción, el sidebar se oculte automáticamente.
    const sidebarLinks = document.querySelectorAll('.sidebar-nav a');
    sidebarLinks.forEach(link => {
        link.addEventListener('click', () => {
            if (window.innerWidth <= 992) { // Solo en pantallas pequeñas
                closeSidebar();
            }
        });
    });

    // Opcional: Asegurarse de que el sidebar se comporte correctamente al redimensionar la ventana
    window.addEventListener('resize', () => {
        if (window.innerWidth > 992) {
            // Si la ventana es grande, asegúrate de que el sidebar esté visible y el overlay oculto
            sidebar.classList.remove('active');
            overlay.classList.remove('active');
            // Asegúrate de que el margen del content-area sea el correcto para desktop
            tableContentArea.style.marginLeft = '250px';
        } else {
            // En pantallas pequeñas, el margen debe ser 0
            tableContentArea.style.marginLeft = '0';
        }
    });

    // Ejecutar al cargar para establecer el estado inicial correcto del margen
    if (window.innerWidth > 992) {
        tableContentArea.style.marginLeft = '250px';
    } else {
        tableContentArea.style.marginLeft = '0';
    }
});

  document.addEventListener('DOMContentLoaded', function() {
            const openMenuButton = document.querySelector('.open-mobile-menu');
            const closeMenuButton = document.querySelector('.close-mobile-menu');
            const fullScreenMenu = document.getElementById('fullScreenMenu');
            const menuLinks = fullScreenMenu.querySelectorAll('ul li a');

            openMenuButton.addEventListener('click', function() {
                fullScreenMenu.classList.add('active');
                // Opcional: ocultar el scroll del body cuando el menú está abierto
                document.body.style.overflow = 'hidden';
            });

            closeMenuButton.addEventListener('click', function() {
                fullScreenMenu.classList.remove('active');
                // Opcional: restaurar el scroll del body
                document.body.style.overflow = '';
            });

            // Cerrar el menú cuando se hace clic en un enlace (para navegación interna)
            menuLinks.forEach(link => {
                link.addEventListener('click', function() {
                    fullScreenMenu.classList.remove('active');
                    document.body.style.overflow = '';
                });
            });
        });