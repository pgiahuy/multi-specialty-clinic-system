export const patientInfoCard = {
    container: {
        backgroundColor: '#f0f7ff',
        border: '1px solid #e9ecef',
        borderRadius: '8px',
        padding: '16px',
        marginBottom: '24px',
        boxShadow: '0 1px 3px rgba(0,0,0,0.08)',
        transition: 'all 0.3s ease'
    },
    grid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
        gap: '16px'
    },
    label: {
        fontSize: '12px',
        fontWeight: '600',
        color: '#6c757d',
        display: 'block',
        marginBottom: '4px'
    },
    value: {
        fontSize: '14px',
        fontWeight: '500',
        color: '#212529',
        margin: 0
    }
};

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
        fontSize: '13px',
        textAlign: 'center',
        color: '#6c757d'
    },
    dataCellLeft: {
        padding: '12px 16px',
        fontSize: '13px',
        fontWeight: '500',
        color: '#212529',
        textAlign: 'left'
    },
    resultCell: {
        padding: '12px 16px',
        fontSize: '13px',
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

export const filterBox = {
    container: {
        padding: '16px',
        border: '1px solid #e9ecef',
        borderRadius: '8px',
        backgroundColor: '#f8f9fa',
        position: 'sticky',
        top: '20px',
        boxShadow: '0 1px 3px rgba(0,0,0,0.05)'
    },
    label: {
        fontWeight: 'bold',
        color: '#495057',
        fontSize: '13px'
    },
    input: {
        borderRadius: '6px',
        border: '1px solid #dee2e6',
        padding: '8px 12px',
        fontSize: '13px'
    },
    button: {
        width: '100%',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        gap: '8px',
        marginBottom: '8px',
        borderRadius: '6px',
        fontWeight: '500'
    }
};
