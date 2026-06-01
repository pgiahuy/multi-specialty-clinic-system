export const tableStyles = {
    container: {
        boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
        borderRadius: '8px',
        overflow: 'hidden'
    },
    table: {
        margin: 0,
        borderCollapse: 'collapse'
    },
    headerRow: {
        backgroundColor: '#0d6efd',
        color: '#fff'
    },
    headerCell: {
        padding: '14px 16px',
        fontWeight: '600',
        fontSize: '14px',
        textAlign: 'center',
        borderBottom: 'none'
    },
    bodyRow: (index) => ({
        backgroundColor: index % 2 === 0 ? '#fff' : '#f8f9fa',
        transition: 'background-color 0.2s ease',
        borderBottom: '1px solid #e9ecef'
    }),
    bodyRowHover: {
        backgroundColor: '#e7f1ff'
    },
    dataCell: {
        padding: '12px 16px',
        fontSize: '14px',
        textAlign: 'center',
        color: '#000000'
    },
    dataCellLeft: {
        padding: '12px 16px',
        fontSize: '14px',
        fontWeight: '500',
        color: '#212529',
        textAlign: 'left'
    },
    resultCell: {
        padding: '12px 16px',
        fontSize: '14px',
        textAlign: 'center',
        color: '#0d6efd',
        fontWeight: '600'
    }
};

export const emptyState = {
    container: {
        textAlign: 'center',
        padding: '20px',
        backgroundColor: '#f8f9fa',
        borderRadius: '8px',
        border: '1px solid #e9ecef'
    },
    title: {
        color: '#6c757d'
    }
};
